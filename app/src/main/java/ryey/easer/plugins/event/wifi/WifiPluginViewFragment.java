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
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;

public class WifiPluginViewFragment extends PluginViewFragment<WifiEventData> {
    private EditText editText_ssid;

    private RadioButton rb_match_essid;
    private RadioButton rb_match_bssid;

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
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle(R.string.wificonn_select_dialog_title);
                //noinspection ConstantConditions
                final ArrayAdapter<WifiConfigurationWrapper> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice);
                for (WifiConfigurationWrapper wrapper : listWifi()) {
                    arrayAdapter.add(wrapper);
                }
                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onWifiSelected(arrayAdapter.getItem(which));
                    }
                });
                builderSingle.show();
            }
        });

        return view;
    }

    private List<WifiConfigurationWrapper> listWifi() {
        List<WifiConfigurationWrapper> list = new ArrayList<>();
        //noinspection ConstantConditions
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
    protected void _fill(@ValidData @NonNull WifiEventData data) {
        rb_match_essid.setChecked(data.mode_essid);
        editText_ssid.setText(Utils.StringCollectionToString(new ArrayList<>(data.ssids), false));
    }

    @ValidData
    @NonNull
    @Override
    public WifiEventData getData() throws InvalidDataInputException {
        return new WifiEventData(editText_ssid.getText().toString(), rb_match_essid.isChecked());
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
