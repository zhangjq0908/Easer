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

package ryey.easer.skills.condition.ringer_mode;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;

import androidx.annotation.NonNull;

import ryey.easer.skills.condition.SkeletonTracker;

public class RingerModeTracker extends SkeletonTracker<RingerModeConditionData> {

    private AudioManager am;
    private RingerModeConditionData condition;
    private int curMode, curVolume;

    private ContentObserver settingsObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            updateTrackerVolume(am.getStreamVolume(AudioManager.STREAM_RING));
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(intent.getAction())) {
                int newMode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1);
                updateTrackerMode(newMode);
            }
        }
    };
    private static IntentFilter intentFilter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);

    RingerModeTracker(Context context, RingerModeConditionData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);
        this.condition = data;
        this.am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void start() {
        context.registerReceiver(broadcastReceiver, intentFilter); // Sticky broadcast, will fire immediately.
        if (condition.ringerMode == AudioManager.RINGER_MODE_NORMAL)
            context.getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, settingsObserver);
    }

    @Override
    public void stop() {
        context.unregisterReceiver(broadcastReceiver);
        if (condition.ringerMode == AudioManager.RINGER_MODE_NORMAL)
            context.getContentResolver().unregisterContentObserver(settingsObserver);
    }

    private void updateTrackerMode(int newMode) {
        if (curMode == newMode)
            return;
        curMode = newMode;
        if (condition.ringerMode == AudioManager.RINGER_MODE_NORMAL && newMode == AudioManager.RINGER_MODE_NORMAL) {
            curVolume = am.getStreamVolume(AudioManager.STREAM_RING);
        }
        newSatisfiedState(condition.match(curMode, curVolume));
    }

    private void updateTrackerVolume(int newVolume) {
        if (curVolume == newVolume)
            return;
        curVolume = newVolume;
        curMode = am.getRingerMode();
        newSatisfiedState(condition.match(curMode, curVolume));
    }
}
