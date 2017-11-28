/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.event.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public class WifiConnSlot extends AbstractSlot {
    private WifiEventData data = null;
    private EventType type = null;

    private int matched_networks = 0;

    final BroadcastReceiver connReceiver = new BroadcastReceiver() {
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
                } else {
                    matched_networks = 0;
                    determine_satisfied();
                }
            }
        }
    };

    final IntentFilter filter;

    {
        filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }

    public WifiConnSlot(Context context) {
        super(context);
    }

    @Override
    public void set(EventData data) {
        if (data instanceof WifiEventData) {
            this.data = (WifiEventData) data;
            type = data.type();
        } else {
            throw new IllegalArgumentTypeException(data.getClass(), WifiEventData.class);
        }
    }

    @Override
    public boolean isValid() {
        return data.isValid();
    }

    @Override
    public void listen() {
        context.registerReceiver(connReceiver, filter);
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(connReceiver);
    }

    @Override
    public void check() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (compare(wifiInfo))
            matched_networks++;
        determine_satisfied();
    }

    private boolean compare(WifiInfo wifiInfo) {
        String ssid = wifiInfo.getSSID();
        if (ssid.startsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        return data.match(ssid);
    }

    private void determine_satisfied() {
        if (type == EventType.any) {
            changeSatisfiedState(matched_networks > 0);
        } else if (type == EventType.none) {
            changeSatisfiedState(matched_networks == 0);
        } else {
            Logger.wtf("Wifi plugin has unrecognized type");
        }
    }
}
