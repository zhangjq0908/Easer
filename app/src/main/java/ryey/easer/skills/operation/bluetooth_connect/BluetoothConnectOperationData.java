/*
 * Copyright (c) 2016 - 2018, 2020 Rui Zhao <renyuneyun@gmail.com> and Daniel Landau
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

package ryey.easer.skills.operation.bluetooth_connect;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.plugin.PluginDataFormat;

public class BluetoothConnectOperationData implements OperationData {

    private static final String K_ADDRESS = "address";
    private static final String K_SERVICE = "service";

    public static final String A2DP = "A2DP";
    public static final String HID_DEVICE = "HID Device";
    public static final String HEADSET = "Headset";
    public static final String HEARING_AID = "Hearing Aid";
    public static final String GATT = "Gatt";
    public static final String GATT_SERVER = "Gatt Server";

    String address;
    String service;

    public BluetoothConnectOperationData(String address, String service) {
        this.address = address;
        this.service = service;
    }

    BluetoothConnectOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    address = jsonObject.getString(K_ADDRESS);
                    service = jsonObject.getString(K_SERVICE);
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }
    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(K_ADDRESS, address);
                    jsonObject.put(K_SERVICE, service);
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
                res = jsonObject.toString();
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (address == null)
            return false;
        if (service == null)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof BluetoothConnectOperationData))
            return false;
        if (!((BluetoothConnectOperationData) obj).isValid())
            return false;
        if (!Utils.nullableEqual(address,
                ((BluetoothConnectOperationData) obj).address))
            return false;
        if (!Utils.nullableEqual(service,
                ((BluetoothConnectOperationData) obj).service))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(service);
    }

    private BluetoothConnectOperationData(Parcel in) {
        address = in.readString();
        service = in.readString();
    }

    public static final Creator<BluetoothConnectOperationData> CREATOR
            = new Creator<BluetoothConnectOperationData>() {
        public BluetoothConnectOperationData createFromParcel(Parcel in) {
            return new BluetoothConnectOperationData(in);
        }

        public BluetoothConnectOperationData[] newArray(int size) {
            return new BluetoothConnectOperationData[size];
        }
    };

    @Nullable
    @Override
    public Set<String> placeholders() {
        Set<String> placeholders = Utils.extractPlaceholder(address);
        placeholders.addAll(Utils.extractPlaceholder(service));
        return placeholders;
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        String new_address = Utils.applyDynamics(address, dynamicsAssignment);
        String new_service = Utils.applyDynamics(service, dynamicsAssignment);
        return new BluetoothConnectOperationData(new_address, new_service);
    }
}
