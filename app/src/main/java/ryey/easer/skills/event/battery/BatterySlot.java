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

package ryey.easer.skills.event.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import ryey.easer.skills.event.AbstractSlot;

public class BatterySlot extends AbstractSlot<BatteryEventData> {

    private int status;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_POWER_CONNECTED:
                    determineAndNotify(true);
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    determineAndNotify(false);
                    break;
            }
        }
    };
    private IntentFilter filter;

    {
        filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
//        filter.addAction(Intent.ACTION_BATTERY_LOW);
//        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }

    public BatterySlot(Context context, BatteryEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    BatterySlot(Context context, BatteryEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        status = data.battery_status;
    }

    @Override
    public void listen() {
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(receiver);
    }

    private void determineAndNotify(boolean isCharging) {
        changeSatisfiedState((status == BatteryStatus.charging) == isCharging);
    }
}
