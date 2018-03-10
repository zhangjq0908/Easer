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

package ryey.easer.plugins.event.headset;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public class HeadsetSlot extends AbstractSlot<HeadsetEventData> {
    private static final String expected_action;

    static {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            expected_action = Intent.ACTION_HEADSET_PLUG;
        } else {
            expected_action = AudioManager.ACTION_HEADSET_PLUG;
        }
    }

    private final IntentFilter mFilter = new IntentFilter(expected_action);
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle extras = intent.getExtras();
            if (expected_action.equals(intent.getAction()) && extras != null) {
                final int state = extras.getInt("state");
                final int microphone = extras.getInt("microphone");
                final boolean match = determine_match(eventData, state == 1, microphone == 1);
                if (eventData.type() == EventType.is) {
                    changeSatisfiedState(match);
                } else {
                    changeSatisfiedState(!match);
                }
            }
        }
    };

    HeadsetSlot(Context context, HeadsetEventData data) {
        this(context, data, (data.type() == EventType.is || data.type() == EventType.is_not), PERSISTENT_DEFAULT);
    }

    HeadsetSlot(Context context, HeadsetEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        context.registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(mReceiver);
    }

    @Override
    public void check() {
    }

    @SuppressWarnings("RedundantIfStatement")
    private static boolean determine_match(HeadsetEventData eventData, boolean plug_in, boolean has_microphone) {
        if (!(eventData.hs_action == HeadsetEventData.HeadsetAction.any ||
                plug_in == (eventData.hs_action == HeadsetEventData.HeadsetAction.plug_in)))
            return false;
        if (!(eventData.hs_type == HeadsetEventData.HeadsetType.any ||
                has_microphone == (eventData.hs_type == HeadsetEventData.HeadsetType.with_microphone)))
            return false;
        return true;
    }

}