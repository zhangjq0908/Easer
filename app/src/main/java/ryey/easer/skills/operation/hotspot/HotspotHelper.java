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

package ryey.easer.skills.operation.hotspot;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.ResultReceiver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.orhanobut.logger.Logger;



import static android.content.Context.CONNECTIVITY_SERVICE;


class HotspotHelper {
    private static HotspotHelper instance = null;
    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;


    private void callStartTethering(Object internalConnectivityManager) throws ReflectiveOperationException {
        Class internalConnectivityManagerClass = Class.forName("android.net.IConnectivityManager");

        ResultReceiver dummyResultReceiver = new ResultReceiver(null);

        try {
            Method startTetheringMethod = internalConnectivityManagerClass.getDeclaredMethod("startTethering",
                    int.class,
                    ResultReceiver.class,
                    boolean.class);

            startTetheringMethod.invoke(internalConnectivityManager,
                    0,
                    dummyResultReceiver,
                    false);
        } catch (NoSuchMethodException e) {
            // Newer devices have "callingPkg" String argument at the end of this method.
            Method startTetheringMethod = internalConnectivityManagerClass.getDeclaredMethod("startTethering",
                    int.class,
                    ResultReceiver.class,
                    boolean.class,
                    String.class);

            startTetheringMethod.invoke(internalConnectivityManager,
                    0,
                    dummyResultReceiver,
                    false,
                    "ryey.easer");
        }
    }

    static synchronized HotspotHelper getInstance(Context context) {
        if (instance != null)
            return instance;
        instance = new HotspotHelper();
        instance.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        instance.connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
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

    boolean setTethering(boolean enable){
        boolean apStatus = false;
        try {
            Class<ConnectivityManager> connectivityClass = ConnectivityManager.class;
            if (enable) {
                Field internalConnectivityManagerField = ConnectivityManager.class.getDeclaredField("mService");
                internalConnectivityManagerField.setAccessible(true);
                callStartTethering(internalConnectivityManagerField.get(connectivityManager));
            } else {
                Method stopTetheringMethod = connectivityClass.getDeclaredMethod("stopTethering", int.class);
                stopTetheringMethod.invoke(connectivityManager, 0);
            }
            apStatus = true;
        } catch (Exception e) {
            apStatus = false;
            Logger.e(e,"Error while changing hotspot state in Tethering method");
        }
        return apStatus;
    }

    boolean setApStatus(WifiConfiguration netConfig, boolean enable) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        boolean apStatus = false;    
        try {
            Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            apStatus = (boolean) setWifiApMethod.invoke(wifiManager, netConfig, enable);
        } catch (NoSuchMethodException e) {
            apStatus = false;
        }
        if (!apStatus) {
            apStatus = setTethering(enable);
        }
        return apStatus;
    }

    int getApState() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getWifiApStateMethod = wifiManager.getClass().getMethod("getWifiApState");
        int apState = (int) getWifiApStateMethod.invoke(wifiManager);
        return apState;
    }
}
