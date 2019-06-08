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

package ryey.easer.skills.usource.bluetooth_device;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;


public class BTDeviceUSourceData implements USourceData {
    final List<String> hwAddresses = new ArrayList<>();

    BTDeviceUSourceData(String[] hardware_addresses) {
        setMultiple(hardware_addresses);
    }

    BTDeviceUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                hwAddresses.clear();
                switch (format) {
                    default:
                        try {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String hwAddr = jsonArray.getString(i);
                                hwAddresses.add(hwAddr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new IllegalStorageDataException(e);
                        }
                }
        }
    }

    private void setMultiple(String[] hardware_addresses) {
        for (String hardware_address : hardware_addresses) {
            if (!Utils.isBlank(hardware_address))
                this.hwAddresses.add(hardware_address.trim());
        }
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (hwAddresses.size() == 0)
            return false;
        return true;
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return new Dynamics[]{new DeviceNameDynamics(), new DeviceAddressDynamics()};
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BTDeviceUSourceData))
            return false;
        if (!hwAddresses.equals(((BTDeviceUSourceData) obj).hwAddresses))
            return false;
        return true;
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONArray jsonArray = new JSONArray();
                for (String hwaddr : hwAddresses) {
                    jsonArray.put(hwaddr);
                }
                res = jsonArray.toString();
        }
        return res;
    }

    public boolean match(@NonNull Object obj) {
        if (obj instanceof String) {
            return hwAddresses.contains(((String) obj).trim());
        }
        return equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(hwAddresses);
    }

    public static final Creator<BTDeviceUSourceData> CREATOR
            = new Creator<BTDeviceUSourceData>() {
        public BTDeviceUSourceData createFromParcel(Parcel in) {
            return new BTDeviceUSourceData(in);
        }

        public BTDeviceUSourceData[] newArray(int size) {
            return new BTDeviceUSourceData[size];
        }
    };

    private BTDeviceUSourceData(Parcel in) {
        in.readStringList(hwAddresses);
    }

    static class DeviceNameDynamics implements Dynamics {

        static final String id = "ryey.easer.skills.combined.bluetooth_device.device_name";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.usource_bt_device_dynamics_device_name;
        }
    }

    static class DeviceAddressDynamics implements Dynamics {

        static final String id = "ryey.easer.skills.combined.bluetooth_device.device_address";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.usource_bt_device_dynamics_device_address;
        }
    }
}
