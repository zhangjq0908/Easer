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

package ryey.easer.plugins.condition.connectivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import ryey.easer.plugins.condition.SkeletonTracker;

import static ryey.easer.plugins.condition.connectivity.ConnectivityType.TYPE_BLUETOOTH;
import static ryey.easer.plugins.condition.connectivity.ConnectivityType.TYPE_ETHERNET;
import static ryey.easer.plugins.condition.connectivity.ConnectivityType.TYPE_MOBILE;
import static ryey.easer.plugins.condition.connectivity.ConnectivityType.TYPE_NOT_CONNECTED;
import static ryey.easer.plugins.condition.connectivity.ConnectivityType.TYPE_VPN;
import static ryey.easer.plugins.condition.connectivity.ConnectivityType.TYPE_WIFI;

public class ConnectivityTracker extends SkeletonTracker<ConnectivityConditionData> {
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    check();
                    break;
            }
        }
    };
    private final IntentFilter filter;

    {
        filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    ConnectivityTracker(Context context, ConnectivityConditionData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);
        check();
    }

    @Override
    public void start() {
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void stop() {
        context.unregisterReceiver(receiver);
    }

    private void check() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        determineAndNotify(convertType(activeNetworkInfo));
    }

    private int convertType(NetworkInfo activeNetworkInfo) {
        if (activeNetworkInfo == null) {
            return TYPE_NOT_CONNECTED;
        }
        switch (activeNetworkInfo.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                return TYPE_WIFI;
            case ConnectivityManager.TYPE_MOBILE:
                return TYPE_MOBILE;
            case ConnectivityManager.TYPE_ETHERNET:
                return TYPE_ETHERNET;
            case ConnectivityManager.TYPE_BLUETOOTH:
                return TYPE_BLUETOOTH;
            case ConnectivityManager.TYPE_VPN:
                return TYPE_VPN;
        }
        return -1;
    }

    private void determineAndNotify(int networkType) {
        if (data.connectivity_type.contains(networkType))
            newSatisfiedState(true);
        else
            newSatisfiedState(false);
    }

}
