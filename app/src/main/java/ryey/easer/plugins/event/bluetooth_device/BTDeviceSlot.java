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

package ryey.easer.plugins.event.bluetooth_device;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public class BTDeviceSlot extends AbstractSlot<BTDeviceEventData> {
    private BTDeviceEventData data = null;
    private EventType type = null;

    private int matched_devices = 0;

    private final BroadcastReceiver connReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (is_target(device)) {
                    matched_devices++;
                    determine_satisfied();
                }
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (is_target(device)) {
                    matched_devices--;
                    determine_satisfied();
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

    public BTDeviceSlot(Context context) {
        super(context);
    }

    @Override
    public void set(@NonNull BTDeviceEventData data) {
        this.data = (BTDeviceEventData) data;
        type = data.type();
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            for (int profile : new int[]{BluetoothProfile.GATT, BluetoothProfile.GATT_SERVER}) {
                for (BluetoothDevice btDevice : bluetoothManager.getConnectedDevices(profile)) {
                    if (is_target(btDevice)) {
                        matched_devices++;
                    }
                }
            }
        }
        determine_satisfied();
    }

    private boolean is_target(BluetoothDevice device) {
        return data.match(device.getAddress());
    }

    private void determine_satisfied() {
        if (type == EventType.any)
            changeSatisfiedState(matched_devices > 0);
        else
            changeSatisfiedState(matched_devices == 0);
    }
}
