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

package ryey.easer.skills.operation.network_transmission;

import android.os.Parcel;
import android.os.Parcelable;

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

public class NetworkTransmissionOperationData implements OperationData {

    private static final String K_PROTOCOL = "protocol";
    private static final String K_REMOTE_ADDRESS = "remote_address";
    private static final String K_REMOTE_PORT = "remote_port";
    private static final String K_DATA = "data";

    @Nullable
    @Override
    public Set<String> placeholders() {
        return null;
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        return this;
    }

    enum Protocol {
        tcp,
        udp,
    }

    Protocol protocol;
    String remote_address;
    int remote_port;
    String data; //TODO: change to byte array to support arbitrary data

    NetworkTransmissionOperationData(Protocol protocol, String remote_address, int remote_port, String data) {
        this.protocol = protocol;
        this.remote_address = remote_address;
        this.remote_port = remote_port;
        this.data = data;
    }

    NetworkTransmissionOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    protocol = Protocol.valueOf(jsonObject.getString(K_PROTOCOL));
                    remote_address = jsonObject.getString(K_REMOTE_ADDRESS);
                    remote_port = jsonObject.getInt(K_REMOTE_PORT);
                    this.data = jsonObject.getString(K_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
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
                    jsonObject.put(K_PROTOCOL, protocol.toString());
                    jsonObject.put(K_REMOTE_ADDRESS, remote_address);
                    jsonObject.put(K_REMOTE_PORT, remote_port);
                    jsonObject.put(K_DATA, data);
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStateException(e.getMessage());
                }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (protocol == null)
            return false;
        if (Utils.isBlank(remote_address))
            return false;
        if (remote_port <= 0)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof NetworkTransmissionOperationData))
            return false;
        if (protocol != ((NetworkTransmissionOperationData) obj).protocol)
            return false;
        if (!remote_address.equals(((NetworkTransmissionOperationData) obj).remote_address))
            return false;
        if (remote_port != ((NetworkTransmissionOperationData) obj).remote_port)
            return false;
        if (!Utils.nullableEqual(data, ((NetworkTransmissionOperationData) obj).data))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(protocol);
        dest.writeString(remote_address);
        dest.writeInt(remote_port);
        dest.writeString(data);
    }

    public static final Parcelable.Creator<NetworkTransmissionOperationData> CREATOR
            = new Parcelable.Creator<NetworkTransmissionOperationData>() {
        public NetworkTransmissionOperationData createFromParcel(Parcel in) {
            return new NetworkTransmissionOperationData(in);
        }

        public NetworkTransmissionOperationData[] newArray(int size) {
            return new NetworkTransmissionOperationData[size];
        }
    };

    private NetworkTransmissionOperationData(Parcel in) {
        protocol = (Protocol) in.readSerializable();
        remote_address = in.readString();
        remote_port = in.readInt();
        data = in.readString();
    }
}
