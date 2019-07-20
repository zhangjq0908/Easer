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

package ryey.easer.skills.usource.power;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

final class Utils {

    static final int []pair1 = {
            BatteryManager.BATTERY_PLUGGED_AC,
            BatteryManager.BATTERY_PLUGGED_USB,
    };
    static final ChargingMethod []pair2 = {
            ChargingMethod.ac,
            ChargingMethod.usb,
    };

    static Intent getBatteryStickyIntent(Context context) {
        return context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    static boolean determine(boolean isCharging, PowerUSourceData data, Context context) {
        if (isCharging == (data.batteryStatus == BatteryStatus.charging)) {
            if (data.batteryStatus == BatteryStatus.charging) {
                return determineCharging(data, getBatteryStickyIntent(context));
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    static boolean determine(boolean isCharging, PowerUSourceData data, Intent batteryStickyIntent) {
        if (isCharging == (data.batteryStatus == BatteryStatus.charging)) {
            if (data.batteryStatus == BatteryStatus.charging) {
                return determineCharging(data, batteryStickyIntent);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    static boolean determineCharging(PowerUSourceData data, Intent batteryStickyIntent) {
        if (data.chargingMethods.contains(ChargingMethod.any))
            return true;
        int plugged = batteryStickyIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        for (int i = 0; i < pair1.length; i++) {
            if (plugged == pair1[i]) {
                return data.chargingMethods.contains(pair2[i]);
            }
        }
        return false;
    }
}
