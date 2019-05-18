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

package ryey.easer.skills.operation.hotspot;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class HotspotHelper {
    private static HotspotHelper instance = null;
    private WifiManager wifiManager;

    static synchronized HotspotHelper getInstance(Context context) {
        if (instance != null)
            return instance;
        instance = new HotspotHelper();
        instance.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return instance;
    }

    boolean enableAp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        boolean apStatus = setApStatus(null, true);
        return apStatus;
    }

    boolean disableAp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        boolean apStatus = setApStatus(null, false);
        return apStatus;
    }

    boolean isApEnabled() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isWifiApEnabledmethod = wifiManager.getClass().getMethod("isWifiApEnabled");
        Boolean isWifiApEnabled = (boolean) isWifiApEnabledmethod.invoke(wifiManager);
        return isWifiApEnabled;
    }

    WifiConfiguration getApConfiguration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
        WifiConfiguration netConfig = (WifiConfiguration) getWifiApConfigurationMethod.invoke(wifiManager);
        return netConfig;
    }

    boolean setApStatus(WifiConfiguration netConfig, boolean enable) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
        boolean apStatus = (boolean) setWifiApMethod.invoke(wifiManager, netConfig, enable);
        return apStatus;
    }

    int getApState() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getWifiApStateMethod = wifiManager.getClass().getMethod("getWifiApState");
        int apState = (int) getWifiApStateMethod.invoke(wifiManager);
        return apState;
    }
}
