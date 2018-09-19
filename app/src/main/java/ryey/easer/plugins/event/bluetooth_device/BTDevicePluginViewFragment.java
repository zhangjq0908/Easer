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

package ryey.easer.plugins.event.bluetooth_device;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.plugins.PluginViewFragment;
import ryey.easer.commons.local_plugin.ValidData;

public class BTDevicePluginViewFragment extends PluginViewFragment<BTDeviceEventData> {
    private final String ACTION_RETURN = "ryey.easer.plugins.event.bluetooth_device.return_from_dialog";
    private final String EXTRA_HARDWARE_ADDRESS = "ryey.easer.plugins.event.bluetooth_device.extra.hardware_address";

    private final IntentFilter mFilter = new IntentFilter(ACTION_RETURN);
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_RETURN)) {
                addHardwareAddress(intent.getStringExtra(EXTRA_HARDWARE_ADDRESS));
                context.unregisterReceiver(mReceiver);
            }
        }
    };

    private EditText editText;
    private TextView textView;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__bluetooth_device, container, false);

        editText = view.findViewById(R.id.hardware_address);
        textView = view.findViewById(R.id.device_name);

        editText.addTextChangedListener(new TextWatcher() {
            final String name_not_found = getResources().getString(R.string.ebtdevice_unknown_device);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] hw_addresses = s.toString().split("\n");
                StringBuilder text = new StringBuilder();
                boolean first_line = true;
                if (hw_addresses.length > 0) {
                    for (String hw_address : hw_addresses) {
                        if (Utils.isBlank(hw_address))
                            continue;
                        if (!first_line)
                            text.append("\n");
                        String name = resolveHWAddress(hw_address);
                        if (name != null)
                            text.append(name);
                        else
                            text.append(name_not_found);
                        first_line = false;
                    }
                }
                textView.setText(text.toString());
            }
        });

        view.findViewById(R.id.connection_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.hasPermission(getContext(), Manifest.permission.BLUETOOTH))
                    return;
                getContext().registerReceiver(mReceiver, mFilter);
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle(R.string.ebtdevice_select_dialog_title);
                final ArrayAdapter<BTDeviceWrapper> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice);
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null) {
                    for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
                        arrayAdapter.add(new BTDeviceWrapper(device));
                    }
                }
                builderSingle.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContext().unregisterReceiver(mReceiver);
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

        return view;
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
    protected void _fill(@ValidData @NonNull BTDeviceEventData data) {
        editText.setText(data.toString());
    }

    @ValidData
    @NonNull
    @Override
    public BTDeviceEventData getData() throws InvalidDataInputException {
        return new BTDeviceEventData(editText.getText().toString().split("\n"));
    }

    class BTDeviceWrapper {
        final BluetoothDevice device;
        BTDeviceWrapper(BluetoothDevice device) {
            this.device = device;
        }
        public String toString() {
            return device.getName();
        }
    }
}
