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

package ryey.easer.skills.usource.wifi;

import android.net.wifi.WifiInfo;

import androidx.annotation.NonNull;

final class Utils {
    static boolean compare(WifiUSourceData data, @NonNull WifiInfo wifiInfo) {
        String ssid;
        if (data.mode_essid) {
            ssid = wifiInfo.getSSID();
            if (ssid.startsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
        } else {
            ssid = wifiInfo.getBSSID();
        }
        return data.match(ssid);
    }

    //TODO: Move BroadcastReceiver content to here
}
