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

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import ryey.easer.R;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.plugins.event.TypedContentLayout;

public class BTDeviceContentLayout extends TypedContentLayout {
    final String ACTION_RETURN = "ryey.easer.plugins.event.bluetooth_device.return_from_dialog";
    final String EXTRA_HARDWARE_ADDRESS = "ryey.easer.plugins.event.bluetooth_device.extra.hardware_address";

    IntentFilter mFilter = new IntentFilter(ACTION_RETURN);
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_RETURN)) {
                setHardwareAddress(intent.getStringExtra(EXTRA_HARDWARE_ADDRESS));
                context.unregisterReceiver(mReceiver);
            }
        }
    };

    EditText editText;
    TextView textView;

    {
        expectedDataClass = BTDeviceEventData.class;
    }

    public BTDeviceContentLayout(final Context context) {
        super(context);
        setAvailableTypes(new BTDeviceEventData().availableTypes());
        setType(new BTDeviceEventData().type());
        setDesc(context.getString(R.string.event_bluetooth_device));
        inflate(context, R.layout.plugin_event__bluetooth_device, this);
        editText = (EditText) findViewById(R.id.hardware_address);
        textView = (TextView) findViewById(R.id.device_name);

        findViewById(R.id.connection_picker).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.registerReceiver(mReceiver, mFilter);
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                builderSingle.setTitle(R.string.ebtdevice_select_dialog_title);
                final ArrayAdapter<BTDeviceWrapper> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice);
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null) {
                    for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
                        arrayAdapter.add(new BTDeviceWrapper(device));
                    }
                }
                builderSingle.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.unregisterReceiver(mReceiver);
                        dialog.dismiss();
                    }
                });
                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String hardware_address = arrayAdapter.getItem(which).device.getAddress();
                        Intent intent = new Intent(ACTION_RETURN);
                        intent.putExtra(EXTRA_HARDWARE_ADDRESS, hardware_address);
                        getContext().sendBroadcast(intent);
                    }
                });
                builderSingle.show();
            }
        });
    }

    private void setHardwareAddress(String hardware_address) {
        editText.setText(hardware_address);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        for (BluetoothDevice btDevice : bluetoothAdapter.getBondedDevices()) {
            if (btDevice.getAddress().equals(hardware_address)) {
                textView.setText(btDevice.getName());
                return;
            }
        }
        textView.setText(R.string.ebtdevice_unknown_device);
    }

    @Override
    protected void _fill(StorageData data) {
        if (data instanceof BTDeviceEventData) {
            setHardwareAddress((String) data.get());
        }
    }

    @Override
    public StorageData getData() {
        return new BTDeviceEventData(editText.getText().toString(), selectedType());
    }

    class BTDeviceWrapper {
        BluetoothDevice device;
        BTDeviceWrapper(BluetoothDevice device) {
            this.device = device;
        }
        public String toString() {
            return device.getName();
        }
    }
}
