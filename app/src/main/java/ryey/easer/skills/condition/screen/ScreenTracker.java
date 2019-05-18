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

package ryey.easer.skills.condition.screen;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import ryey.easer.skills.condition.SkeletonTracker;

public class ScreenTracker extends SkeletonTracker<ScreenConditionData> {

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                newSatisfiedState(data.screenEvent == ScreenConditionData.ScreenEvent.on);
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                newSatisfiedState(data.screenEvent == ScreenConditionData.ScreenEvent.off);
            } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                newSatisfiedState(data.screenEvent == ScreenConditionData.ScreenEvent.unlocked);
            }
        }
    };

    private final IntentFilter intentFilter;
    
    {
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
    }

    ScreenTracker(Context context, ScreenConditionData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);
        
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm == null) {
            Logger.e("ScreenTracker can't get PowerManager");
            return;
        }
        if (pm.isScreenOn()) {
            newSatisfiedState(data.screenEvent == ScreenConditionData.ScreenEvent.on);
        } else {
            newSatisfiedState(data.screenEvent == ScreenConditionData.ScreenEvent.off);
        }
    }

    @Override
    public void start() {
        context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void stop() {
        context.registerReceiver(mReceiver, intentFilter);
    }
}
