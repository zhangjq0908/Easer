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

package ryey.easer.plugins.event.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public class BatterySlot extends AbstractSlot<BatteryEventData> {

    private int status;
    private EventType type;

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
        type = data.type();
    }

    @Override
    public void listen() {
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(receiver);
    }

    @Override
    public void check() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                             status == BatteryManager.BATTERY_STATUS_FULL;
        determineAndNotify(isCharging);
    }

    private void determineAndNotify(boolean isCharging) {
        if ((status == BatteryStatus.charging) == isCharging) {
            changeSatisfiedState(type == EventType.is);
        } else {
            changeSatisfiedState(type == EventType.is_not);
        }
    }
}
