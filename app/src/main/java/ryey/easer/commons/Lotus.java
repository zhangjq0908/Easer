/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.commons;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.AbstractSlot;
import ryey.easer.core.ProfileLoaderIntentService;

public class Lotus {
    static final String ACTION_SLOT_SATISFIED = "ryey.easer.triggerlotus.action.SLOT_SATISFIED";
    static final String CATEGORY_NOTIFY_LOTUS = "ryey.easer.triggerlotus.category.NOTIFY_LOTUS";

    Context context;
    final String event;
    final String profile;

    final Uri uri = Uri.parse(String.format("lotus://%d", hashCode()));
    final PendingIntent notifyLotusIntent;

    PendingIntent mPendingIntent;

    List<AbstractSlot> slots = new ArrayList<>();

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SLOT_SATISFIED))
                onSlotSatisfied();
        }
    };

    public Lotus(Context context, String event, String profile) {
        this.context = context;
        this.event = event;
        this.profile = profile;

        Intent intent = new Intent(context, ProfileLoaderIntentService.class);
        intent.setAction(ProfileLoaderIntentService.ACTION_LOAD_PROFILE);
        intent.putExtra(ProfileLoaderIntentService.EXTRA_PROFILE_NAME, profile);
        mPendingIntent = PendingIntent.getService(context, 0, intent, 0);

        Intent intent1 = new Intent(ACTION_SLOT_SATISFIED);
        intent1.addCategory(CATEGORY_NOTIFY_LOTUS);
        intent1.setData(uri);
        notifyLotusIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SLOT_SATISFIED);
        filter.addCategory(CATEGORY_NOTIFY_LOTUS);
        filter.addDataScheme(uri.getScheme());
        filter.addDataAuthority(uri.getAuthority(), null);
        filter.addDataPath(uri.getPath(), PatternMatcher.PATTERN_LITERAL);
        context.registerReceiver(mReceiver, filter);
    }

    public void register(AbstractSlot slot) {
        if (slot == null) {
            Log.wtf(getClass().getSimpleName(), "trying to register a null slot");
            return;
        }
        if (slot.isValid()) {
            slot.onRegister(notifyLotusIntent);
            slots.add(slot);
        } else {
            Log.w(getClass().getSimpleName(), "trying to register an invalid slot:" + slot.getClass().getSimpleName());
        }
    }

    public void apply() {
        for (AbstractSlot slot : slots)
            slot.apply();
    }

    public void cancel() {
        for (AbstractSlot slot : slots)
            slot.cancel();
    }

    synchronized void onSlotSatisfied() {
        Log.d(getClass().getSimpleName(), "onSlotSatisfied");
        for (AbstractSlot slot : slots) {
            if (!slot.isSatisfied()) {
                return;
            }
        }
        Log.d(getClass().getSimpleName(), " all satisfied");
        try {
            mPendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
