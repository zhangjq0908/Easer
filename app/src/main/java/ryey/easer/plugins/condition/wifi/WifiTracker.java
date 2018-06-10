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

package ryey.easer.plugins.condition.wifi;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import ryey.easer.plugins.condition.SkeletonTracker;

public class WifiTracker extends SkeletonTracker<WifiConditionData> {

    private int matched_networks = 0;

    private final BroadcastReceiver connReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                    if (compare(wifiInfo))
                        matched_networks++;
                    determine_satisfied();
                } else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                    matched_networks = 0;
                    determine_satisfied();
                }
            }
        }
    };

    private final IntentFilter filter;

    {
        filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }

    WifiTracker(Context context, WifiConditionData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (compare(wifiInfo))
            matched_networks++;
        determine_satisfied();
    }

    @Override
    public void start() {
        context.registerReceiver(connReceiver, filter);
    }

    @Override
    public void stop() {
        context.unregisterReceiver(connReceiver);
    }

    private boolean compare(WifiInfo wifiInfo) {
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

    private void determine_satisfied() {
        newSatisfiedState(matched_networks > 0);
    }
}
