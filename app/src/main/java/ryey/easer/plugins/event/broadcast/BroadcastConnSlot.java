/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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
import android.support.annotation.NonNull;

import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public class BroadcastConnSlot extends AbstractSlot<BroadcastEventData> {
    private ReceiverSideIntentData intentData = null;
    private EventType type = null;

    private final BroadcastReceiver connReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            changeSatisfiedState(true);
        }
    };

    private IntentFilter filter;

    {
        filter = new IntentFilter();
        setRetriggerable(true);
    }

    public BroadcastConnSlot(Context context) {
        super(context);
    }

    @Override
    public void set(@ValidData @NonNull BroadcastEventData data) {
        intentData = data.intentData;
        type = data.type();
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

    @Override
    public boolean canPromoteSub() {
        if (type == EventType.is) {
            return false;
        } else if (type == EventType.after) {
            return true;
        }
        throw new IllegalAccessError();
    }

}
