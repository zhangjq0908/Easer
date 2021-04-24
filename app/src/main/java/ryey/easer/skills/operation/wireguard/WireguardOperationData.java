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

package ryey.easer.skills.operation.wireguard;

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

public class WireguardOperationData implements OperationData {
    private static final String K_TUNNEL_NAME = "tunnel_name";
    private static final String K_TUNNEL_STATE = "tunnel_state";

    final String tunnel_name;
    final Boolean tunnel_state;

    WireguardOperationData(@NonNull String tunnel_name, @NonNull Boolean tunnel_state) {
        this.tunnel_name = tunnel_name;
        this.tunnel_state = tunnel_state;
    }

    WireguardOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    tunnel_name = jsonObject.getString(K_TUNNEL_NAME);
                    tunnel_state = jsonObject.getBoolean(K_TUNNEL_STATE);
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String s = "";
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(K_TUNNEL_NAME, tunnel_name);
                    jsonObject.put(K_TUNNEL_STATE, tunnel_state);
                    s = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        return s;
    }

    @Override
    public boolean isValid() {
        return !Utils.isBlank(tunnel_name) && tunnel_state != null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof WireguardOperationData))
            return false;
        if (!Utils.nullableEqual(tunnel_name, ((WireguardOperationData) obj).tunnel_name))
            return false;
        if (!Utils.nullableEqual(tunnel_state, ((WireguardOperationData) obj).tunnel_state))
            return false;
        return true;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tunnel_name);
        dest.writeByte((byte) (tunnel_state ? 1 : 0));
    }

    public static final Creator<WireguardOperationData> CREATOR
            = new Creator<WireguardOperationData>() {
        public WireguardOperationData createFromParcel(Parcel in) {
            return new WireguardOperationData(in);
        }

        public WireguardOperationData[] newArray(int size) {
            return new WireguardOperationData[size];
        }
    };

    private WireguardOperationData(Parcel in) {
        tunnel_name = in.readString();
        tunnel_state = in.readByte() != 0;
    }
}
