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

package ryey.easer.skills.condition.wifi_enabled;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import ryey.easer.skills.condition.SkeletonTracker;

public class WifiEnabledTracker extends SkeletonTracker<WifiEnabledConditionData> {

    private WifiManager wifiManager;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

                switch (extraWifiState)
                {
                    case WifiManager.WIFI_STATE_DISABLED:
                        newSatisfiedState(!data.enabled);
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        newSatisfiedState(data.enabled);
                        break;
                    default:
                        newSatisfiedState(null);
                }
            }
        }
    };
    private final IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);

    WifiEnabledTracker(Context context, WifiEnabledConditionData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);

        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public void start() {
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void stop() {
        context.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public Boolean state() {
        if (wifiManager == null)
            return null;
        return wifiManager.isWifiEnabled();
    }
}
