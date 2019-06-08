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

package ryey.easer.skills.usource.bluetooth_enabled;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import ryey.easer.skills.event.AbstractSlot;

public class BluetoothEnabledSlot extends AbstractSlot<BluetoothEnabledUSourceData> {

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)) {
                    case BluetoothAdapter.STATE_ON:
                        changeSatisfiedState(eventData.enabled);
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        changeSatisfiedState(!eventData.enabled);
                        break;
                }
            }
        }
    };
    private static IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);

    public BluetoothEnabledSlot(Context context, BluetoothEnabledUSourceData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    public BluetoothEnabledSlot(@NonNull Context context, @NonNull BluetoothEnabledUSourceData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(broadcastReceiver);
    }
}
