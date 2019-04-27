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

package ryey.easer.plugins.condition.bluetooth_device;

import android.os.Parcel;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.Utils;
import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionData;
import ryey.easer.plugin.PluginDataFormat;

public class BTDeviceConditionData implements ConditionData {
    final List<String> hwaddresses = new ArrayList<>();

    BTDeviceConditionData(String[] hardware_addresses) {
        setMultiple(hardware_addresses);
    }

    BTDeviceConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                hwaddresses.clear();
                switch (format) {
                    default:
                        try {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String hwaddr = jsonArray.getString(i);
                                hwaddresses.add(hwaddr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new IllegalStorageDataException(e);
                        }
                }
        }
    }

    //TODO: remove redundancy (many plugins use a similar approach)
    private void setMultiple(String[] hardware_addresses) {
        for (String hardware_address : hardware_addresses) {
            if (!Utils.isBlank(hardware_address))
                this.hwaddresses.add(hardware_address.trim());
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONArray jsonArray = new JSONArray();
                for (String hwaddr : hwaddresses) {
                    jsonArray.put(hwaddr);
                }
                res = jsonArray.toString();
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (hwaddresses.size() == 0)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof BTDeviceConditionData))
            return false;
        if (!hwaddresses.equals(((BTDeviceConditionData) obj).hwaddresses))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(hwaddresses);
    }

    public static final Creator<BTDeviceConditionData> CREATOR
            = new Creator<BTDeviceConditionData>() {
        public BTDeviceConditionData createFromParcel(Parcel in) {
            return new BTDeviceConditionData(in);
        }

        public BTDeviceConditionData[] newArray(int size) {
            return new BTDeviceConditionData[size];
        }
    };

    private BTDeviceConditionData(Parcel in) {
        in.readStringList(hwaddresses);
    }
}
