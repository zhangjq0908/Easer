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

package ryey.easer.skills.condition.headset;

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

import ryey.easer.skills.condition.SkeletonTracker;

public class HeadsetTracker extends SkeletonTracker<HeadsetConditionData> {
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
                final boolean match = determine_match(data, state == 1, microphone == 1);
                newSatisfiedState(match);
            }
        }
    };

    HeadsetTracker(Context context, HeadsetConditionData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);

        Boolean plugged_in = null;
        Boolean has_microphone = null;

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
            for (AudioDeviceInfo deviceInfo : audioDevices) {
                if (deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                        || deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                    plugged_in = true;
                    has_microphone = deviceInfo.isSource();
                }
            }
            if (plugged_in == null)
                plugged_in = false;
        } else {
            plugged_in = audioManager.isWiredHeadsetOn();
        }
        if (plugged_in == (data.hs_state == HeadsetConditionData.HeadsetState.plugged_in)) {
            if (data.hs_type == HeadsetConditionData.HeadsetType.any) {
                newSatisfiedState(true);
            } else {
                if (has_microphone != null)
                    newSatisfiedState(
                            has_microphone ==
                                    (data.hs_type == HeadsetConditionData.HeadsetType.with_microphone)
                    );
            }
        } else {
            newSatisfiedState(false);
        }
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
    private static boolean determine_match(HeadsetConditionData data, boolean plugging_in, boolean has_microphone) {
        if (!(plugging_in == (data.hs_state == HeadsetConditionData.HeadsetState.plugged_in)))
            return false;
        if (!(data.hs_type == HeadsetConditionData.HeadsetType.any ||
                has_microphone == (data.hs_type == HeadsetConditionData.HeadsetType.with_microphone)))
            return false;
        return true;
    }
}
