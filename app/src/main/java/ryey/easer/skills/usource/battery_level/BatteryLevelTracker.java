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

package ryey.easer.skills.usource.battery_level;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import ryey.easer.skills.condition.SkeletonTracker;

import static android.content.Intent.ACTION_BATTERY_CHANGED;

public class BatteryLevelTracker extends SkeletonTracker<BatteryLevelUSourceData> {

    private BroadcastReceiver customReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            newSatisfiedState(Utils.levelState(intent, data));
        }
    };

    private static final IntentFilter intentFilter = new IntentFilter(ACTION_BATTERY_CHANGED);

    BatteryLevelTracker(Context context, BatteryLevelUSourceData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);
        if (data.type == BatteryLevelUSourceData.Type.custom) {
            Intent status = context.registerReceiver(null, intentFilter);
            if (status != null) {  // assert status != null;
                boolean state = Utils.levelState(status, data);
                newSatisfiedState(state);
            }
        } else {
            throw new IllegalStateException("data should only be custom in BatteryLevelTracker");
        }
    }

    @Override
    public void start() {
        context.registerReceiver(customReceiver, intentFilter);
    }

    @Override
    public void stop() {
        context.unregisterReceiver(customReceiver);
    }
}
