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

package ryey.easer.skills.event.bluetooth_device;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import ryey.easer.skills.event.AbstractSlot;

public class BTDeviceSlot extends AbstractSlot<BTDeviceEventData> {

    private static Bundle dynamicsForCurrentDevice(BluetoothDevice bluetoothDevice) {
        Bundle bundle = new Bundle();
        bundle.putString(BTDeviceEventData.DeviceNameDynamics.id, bluetoothDevice.getName());
        bundle.putString(BTDeviceEventData.DeviceAddressDynamics.id, bluetoothDevice.getAddress());
        return bundle;
    }

    private int matched_devices = 0;

    private final BroadcastReceiver connReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (is_target(device)) {
                    matched_devices++;
                    determine_satisfied(dynamicsForCurrentDevice(device));
                }
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (is_target(device)) {
                    matched_devices--;
                    determine_satisfied(dynamicsForCurrentDevice(device));
                }
            }
        }
    };

    private final IntentFilter filter;

    {
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
    }

    public BTDeviceSlot(Context context, BTDeviceEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    BTDeviceSlot(Context context, BTDeviceEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        context.registerReceiver(connReceiver, filter);
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(connReceiver);
    }

    private boolean is_target(BluetoothDevice device) {
        return eventData.match(device.getAddress());
    }

    private void determine_satisfied(Bundle dynamics) {
        changeSatisfiedState(matched_devices > 0, dynamics);
    }
}
