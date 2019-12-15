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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import ryey.easer.skills.event.AbstractSlot;

import static android.content.Intent.ACTION_BATTERY_CHANGED;
import static android.content.Intent.ACTION_BATTERY_LOW;
import static android.content.Intent.ACTION_BATTERY_OKAY;

public class BatteryLevelSlot extends AbstractSlot<BatteryLevelUSourceData> {

    private BroadcastReceiver systemReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            changeSatisfiedState(true);
        }
    };
    private BroadcastReceiver customReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            changeSatisfiedState(Utils.levelState(intent, eventData));
        }
    };

    BatteryLevelSlot(Context context, BatteryLevelUSourceData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    BatteryLevelSlot(Context context, BatteryLevelUSourceData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        IntentFilter intentFilter = new IntentFilter();
        if (eventData.type == BatteryLevelUSourceData.Type.system) {
            switch (((BatteryLevelUSourceData.SystemLevel) eventData.level).levelChoice) {
                case low:
                    intentFilter.addAction(ACTION_BATTERY_LOW);
                    break;
                case ok_after_low:
                    intentFilter.addAction(ACTION_BATTERY_OKAY);
                    break;
                default:
                    throw new IllegalStateException("system event should either be low or ok_after_low");
            }
            context.registerReceiver(systemReceiver, intentFilter);
        } else if (eventData.type == BatteryLevelUSourceData.Type.custom) {
            intentFilter.addAction(ACTION_BATTERY_CHANGED);
            context.registerReceiver(customReceiver, intentFilter);
        } else {
            throw new IllegalStateException("battery level data should either be system or custom");
        }
    }

    @Override
    public void cancel() {
        if (eventData.type == BatteryLevelUSourceData.Type.system) {
            context.unregisterReceiver(systemReceiver);
        } else {
            context.unregisterReceiver(customReceiver);
        }
    }

}
