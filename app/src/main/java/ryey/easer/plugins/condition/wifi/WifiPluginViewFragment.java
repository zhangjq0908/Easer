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

package ryey.easer.plugins.condition.wifi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.PluginViewFragment;

public class WifiPluginViewFragment extends PluginViewFragment<WifiConditionData> {
    private EditText editText_ssid;

    private RadioButton rb_match_essid;
    private RadioButton rb_match_bssid;

    private WifiManager wifiManager;
    private boolean waiting_for_result;
    private ReentrantLock wait_lock = new ReentrantLock();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                onResultsAvailable();
            }
        }
    };
    ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        context.registerReceiver(mReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.plugin_event__wifi_connection, container, false);
        editText_ssid = view.findViewById(R.id.wifi_name);

        rb_match_essid = view.findViewById(R.id.rb_match_essid);
        rb_match_bssid = view.findViewById(R.id.rb_match_bssid);

        view.findViewById(R.id.connection_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.hasPermission(getContext(), Manifest.permission.ACCESS_WIFI_STATE))
                    return;
                wait_lock.lock();
                try {
                    waiting_for_result = true;
                } finally {
                    wait_lock.unlock();
                }
                wifiManager.startScan();
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle(R.string.wificonn_wait_for_result);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
        });

        return view;
    }
    
    @Override
    public void onDetach() {
        //noinspection ConstantConditions
        @NonNull Context context = getContext();
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        context.unregisterReceiver(mReceiver);
        super.onDetach();
    }
    
    private void onResultsAvailable() {
        wait_lock.lock();
        try {
            if (!waiting_for_result)
                return;
            waiting_for_result = false;
        } finally {
            wait_lock.unlock();
        }
        final ArrayAdapter<WifiConfigurationWrapper> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice);
        for (WifiConfigurationWrapper wrapper : obtainWifiList()) {
            arrayAdapter.add(wrapper);
        }
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle(R.string.wificonn_select_dialog_title);
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onWifiSelected(arrayAdapter.getItem(which));
            }
        });
        builderSingle.show();
        progressDialog.dismiss();
    }

    private List<WifiConfigurationWrapper> obtainWifiList() {
        List<WifiConfigurationWrapper> list = new ArrayList<>();
        if (wifiManager != null) {
            List<ScanResult> scanResults = wifiManager.getScanResults();
            for (ScanResult result : scanResults) {
                list.add(new WifiConfigurationWrapper(result.SSID, result.BSSID));
            }
        }
        return list;
    }

    private void onWifiSelected(WifiConfigurationWrapper wrapper) {
        if (rb_match_essid.isChecked()) {
            String essid = wrapper.SSID;
            addESSID(essid);
        } else {
            String bssid = wrapper.BSSID;
            addBSSID(bssid);
        }
    }

    private void addESSID(String essid) {
        Editable text = editText_ssid.getText();
        if (!Utils.isBlank(text.toString()))
            text.append("\n");
        if (essid.startsWith("\"")) {
            essid = essid.substring(1, essid.length() - 1);
        }
        text.append(essid);
    }

    private void addBSSID(String bssid) {
        Editable text = editText_ssid.getText();
        if (!Utils.isBlank(text.toString()))
            text.append("\n");
        text.append(bssid);
    }
    
    @Override
    protected void _fill(@ValidData @NonNull WifiConditionData data) {
        rb_match_essid.setChecked(data.mode_essid);
        editText_ssid.setText(Utils.StringCollectionToString(data.ssids, false));
    }

    @ValidData
    @NonNull
    @Override
    public WifiConditionData getData() throws InvalidDataInputException {
        return new WifiConditionData(editText_ssid.getText().toString(), rb_match_essid.isChecked());
    }

    static class WifiConfigurationWrapper implements Parcelable {
        final String SSID;
        final String BSSID;

        WifiConfigurationWrapper(String SSID, String BSSID) {
            this.SSID = SSID;
            this.BSSID = BSSID;
        }

        private WifiConfigurationWrapper(Parcel in) {
            SSID = in.readString();
            BSSID = in.readString();
        }

        public static final Creator<WifiConfigurationWrapper> CREATOR = new Creator<WifiConfigurationWrapper>() {
            @Override
            public WifiConfigurationWrapper createFromParcel(Parcel in) {
                return new WifiConfigurationWrapper(in);
            }

            @Override
            public WifiConfigurationWrapper[] newArray(int size) {
                return new WifiConfigurationWrapper[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(SSID);
            parcel.writeString(BSSID);
        }

        @Override
        public String toString() {
            return String.format("%s\n[%s]", SSID, BSSID);
        }
    }
}
