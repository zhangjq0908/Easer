/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.plugins.event.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;

import ryey.easer.plugins.event.AbstractSlot;

public class BroadcastConnSlot extends AbstractSlot<BroadcastEventData> {

    private static Bundle dynamicsForCurrent(@NonNull Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putString(BroadcastEventData.ActionDynamics.id, intent.getAction());
        bundle.putStringArray(BroadcastEventData.CategoryDynamics.id, intent.getCategories().toArray(new String[0]));
        bundle.putString(BroadcastEventData.TypeDynamics.id, intent.getType());
        bundle.putString(BroadcastEventData.DataDynamics.id, intent.getDataString());
        return bundle;
    }

    private ReceiverSideIntentData intentData = null;

    private final BroadcastReceiver connReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            changeSatisfiedState(true, dynamicsForCurrent(intent));
        }
    };

    private IntentFilter filter;

    {
        filter = new IntentFilter();
    }

    public BroadcastConnSlot(Context context, BroadcastEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    BroadcastConnSlot(Context context, BroadcastEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        intentData = data.intentData;
        filter = new IntentFilter();
        if (intentData.action != null) {
            for (String action : intentData.action) {
                filter.addAction(action);
            }
        }
        if (intentData.category != null) {
            for (String category : intentData.category) {
                filter.addCategory(category);
            }
        }
    }

    @Override
    public void listen() {
        context.registerReceiver(connReceiver, filter);
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(connReceiver);
    }

    @Override
    public void check() {
        // Empty method as expected
    }

}
