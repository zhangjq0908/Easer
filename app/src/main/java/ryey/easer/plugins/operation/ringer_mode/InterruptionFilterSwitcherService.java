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

package ryey.easer.plugins.operation.ringer_mode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.ConditionVariable;
import android.service.notification.NotificationListenerService;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;

import com.orhanobut.logger.Logger;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class InterruptionFilterSwitcherService extends NotificationListenerService {
    private static final String ACTION_CHANGE = "ryey.easer.plugins.operation.ringer_mode.action.CHANGE";
    private static final String EXTRA_MODE = "ryey.easer.plugins.operation.ringer_mode.extra.MODE";

    private ConditionVariable cv = new ConditionVariable();

    private final IntentFilter mFilter = new IntentFilter(ACTION_CHANGE);
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null || !action.equals(ACTION_CHANGE))
                throw new IllegalAccessError();
            int mode = intent.getIntExtra(EXTRA_MODE, -1);
            cv.block();
            requestInterruptionFilter(mode);
        }
    };

    static void setInterruptionFilter(Context context, int mode) {
        Intent intent = new Intent(ACTION_CHANGE);
        intent.putExtra(EXTRA_MODE, mode);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onListenerConnected() {
        cv.open();
    }

    @Override
    public void onCreate() {
        Logger.i("InterruptionFilterSwitcherService onCreate()");
        super.onCreate();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        Logger.i("InterruptionFilterSwitcherService onDestroy()");
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
    }
}
