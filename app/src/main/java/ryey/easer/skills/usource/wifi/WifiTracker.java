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

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import ryey.easer.skills.condition.SkeletonTracker;

public class WifiTracker extends SkeletonTracker<WifiUSourceData> {

    private final BroadcastReceiver connReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo == null) {
                    newSatisfiedState(null);
                    return;
                }
                if (networkInfo.isConnected()) {
                    WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                    if (wifiInfo == null) {
                        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        if (wifiManager == null) {
                            Logger.wtf("[WifiTracker] WifiManager is null");
                            return;
                        }
                        wifiInfo = wifiManager.getConnectionInfo();
                        Logger.d(wifiInfo);
                    }
                    compareAndSignal(wifiInfo);
                } else if (!networkInfo.isConnectedOrConnecting()) {
                    WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (wifiManager == null) {
                        Logger.wtf("[WifiTracker] WifiManager is null");
                        return;
                    }
                    boolean wifiEnabled = wifiManager.isWifiEnabled();
                    if (!wifiEnabled) {
                        newSatisfiedState(null);
                        return;
                    }
                    newSatisfiedState(false);
                }
            }
        }
    };

    private final IntentFilter filter;

    {
        filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }

    WifiTracker(Context context, WifiUSourceData data,
                @NonNull PendingIntent event_positive,
                @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            Logger.wtf("[WifiTracker] WifiManager is null");
            return;
        }
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            compareAndSignal(wifiInfo);
        }
    }

    @Override
    public void start() {
        context.registerReceiver(connReceiver, filter);
    }

    @Override
    public void stop() {
        context.unregisterReceiver(connReceiver);
    }

    private void compareAndSignal(WifiInfo wifiInfo) {
        boolean match = compare(data, wifiInfo);
        newSatisfiedState(match);
    }

    private static boolean compare(WifiUSourceData data, WifiInfo wifiInfo) {
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
}
