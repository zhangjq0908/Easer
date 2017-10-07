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

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.plugins.event.TypedContentLayout;

public class WifiContentLayout extends TypedContentLayout {
    EditText editText;
    final String ACTION_RETURN = "ryey.easer.plugins.event.bluetooth_device.return_from_dialog";
    final String EXTRA_SSID = "ryey.easer.plugins.event.bluetooth_device.extra.hardware_address";

    IntentFilter mFilter = new IntentFilter(ACTION_RETURN);
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_RETURN)) {
                addSSID(intent.getStringExtra(EXTRA_SSID));
                context.unregisterReceiver(mReceiver);
            }
        }
    };

    {
        expectedDataClass = WifiEventData.class;
    }

    public WifiContentLayout(Context context) {
        super(context);
        setAvailableTypes(new WifiEventData().availableTypes());
        setType(new WifiEventData().type());
        setDesc(context.getString(R.string.event_wificonn));
        inflate(context, R.layout.plugin_event__wifi_connection, this);
        editText = (EditText) findViewById(R.id.wifi_name);

        findViewById(R.id.connection_picker).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.hasPermission(getContext(), Manifest.permission.ACCESS_WIFI_STATE))
                    return;
                getContext().registerReceiver(mReceiver, mFilter);
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle(R.string.ebtdevice_select_dialog_title);
                final ArrayAdapter<WifiDeviceWrapper> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice);
                WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    for (WifiConfiguration configuration : wifiManager.getConfiguredNetworks()) {
                        arrayAdapter.add(new WifiDeviceWrapper(configuration));
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
                        String ssid = arrayAdapter.getItem(which).configuration.SSID;
                        Intent intent = new Intent(ACTION_RETURN);
                        intent.putExtra(EXTRA_SSID, ssid);
                        getContext().sendBroadcast(intent);
                    }
                });
                builderSingle.show();
            }
        });
    }

    private void addSSID(String ssid) {
        Editable text = editText.getText();
        if (!Utils.isBlank(text.toString()))
            text.append("\n");
        if (ssid.startsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        text.append(ssid);
    }

    @Override
    protected void _fill(StorageData data) {
        if (data instanceof WifiEventData) {
            editText.setText(data.toString());
        }
    }

    @Override
    public StorageData getData() {
        return new WifiEventData(editText.getText().toString(), selectedType());
    }

    class WifiDeviceWrapper {
        WifiConfiguration configuration;
        WifiDeviceWrapper(WifiConfiguration configuration) {
            this.configuration = configuration;
        }
        public String toString() {
            return configuration.SSID;
        }
    }
}
