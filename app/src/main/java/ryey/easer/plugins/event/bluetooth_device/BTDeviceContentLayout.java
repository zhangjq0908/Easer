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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import ryey.easer.R;
import ryey.easer.Utils;
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
                addHardwareAddress(intent.getStringExtra(EXTRA_HARDWARE_ADDRESS));
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

        editText.addTextChangedListener(new TextWatcher() {
            String name_not_found = getResources().getString(R.string.ebtdevice_unknown_device);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] hw_addresses = s.toString().split("\n");
                String text = "";
                boolean first_line = true;
                if (hw_addresses.length > 0) {
                    for (String hw_address : hw_addresses) {
                        if (Utils.isBlank(hw_address))
                            continue;
                        if (!first_line)
                            text += "\n";
                        String name = resolveHWAddress(hw_address);
                        if (name != null)
                            text += name;
                        else
                            text += name_not_found;
                        first_line = false;
                    }
                }
                textView.setText(text);
            }
        });

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

    private String resolveHWAddress(String hwaddress) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            for (BluetoothDevice btDevice : bluetoothAdapter.getBondedDevices()) {
                if (btDevice.getAddress().equals(hwaddress)) {
                    return btDevice.getName();
                }
            }
        }
        return null;
    }

    private void addHardwareAddress(String hardware_address) {
        Editable text = editText.getText();
        if (!Utils.isBlank(text.toString()))
            text.append("\n");
        text.append(hardware_address);
    }

    @Override
    protected void _fill(StorageData data) {
        if (data instanceof BTDeviceEventData) {
            editText.setText(data.toString());
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
