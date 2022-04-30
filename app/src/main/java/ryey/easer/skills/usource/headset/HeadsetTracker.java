/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.skills.usource.headset;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.skills.condition.SkeletonTracker;

public class HeadsetTracker extends SkeletonTracker<HeadsetUSourceData> {
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
                final Boolean match = determine_match(data, state == 1, microphone == 1);
                newSatisfiedState(match);
            }
        }
    };

    HeadsetTracker(Context context, HeadsetUSourceData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);

        boolean plugged_in = false;
        Boolean has_microphone = null;

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // noinspection WrongConstant    Lint is wrong in this case
            AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
            for (AudioDeviceInfo deviceInfo : audioDevices) {
                if (deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                        || deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                    plugged_in = true;
                    has_microphone = deviceInfo.isSource();
                }
            }
        } else {
            plugged_in = audioManager.isWiredHeadsetOn();
        }
        Boolean match = determine_match(data, plugged_in, has_microphone);
        newSatisfiedState(match);
    }

    @Override
    public void start() {
        context.registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void stop() {
        context.unregisterReceiver(mReceiver);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Nullable
    static Boolean determine_match(@NonNull HeadsetUSourceData data, boolean plugging_in, @Nullable Boolean has_microphone) {
        if (!(data.hs_state == HeadsetUSourceData.HeadsetState.any ||
                plugging_in == (data.hs_state == HeadsetUSourceData.HeadsetState.plug_in)))
            return false;
        if (data.hs_type == HeadsetUSourceData.HeadsetType.any)
            return true;
        if (has_microphone == null)
            return null;
        return has_microphone == (data.hs_type == HeadsetUSourceData.HeadsetType.with_microphone);
    }
}
