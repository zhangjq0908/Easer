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

package ryey.easer.skills.usource.headset;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

public class HeadsetUSourceData implements USourceData {

    enum HeadsetState {
        plug_in,
        plug_out,
        any,
        @Deprecated
        plugged_in,
        @Deprecated
        plugged_out,
    }

    enum HeadsetType {
        with_microphone,
        without_microphone,
        any,
    }

    @Deprecated
    private static final String K_HS_ACTION = "headset_action";
    private static final String K_HS_STATE = "headset_state";
    private static final String K_HS_TYPE = "headset_type";

    final HeadsetState hs_state;
    final HeadsetType hs_type;

    HeadsetUSourceData(@NonNull HeadsetState hs_state, @NonNull HeadsetType hs_type) {
        this.hs_state = hs_state;
        this.hs_type = hs_type;
    }

    HeadsetUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                String name;
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    hs_state = getHSState(jsonObject, version);
                    name = jsonObject.optString(K_HS_TYPE);
                    if (name != null)
                        hs_type = HeadsetType.valueOf(name);
                    else
                        hs_type = HeadsetType.any;
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    private static HeadsetState getHSState(JSONObject jsonObject, int version) throws JSONException {
        String name;
        HeadsetState hs_state;
        if (version < C.VERSION_UNIFORMED_SOURCE) {
            name = jsonObject.optString(K_HS_ACTION);
            if (name != null)
                hs_state = HeadsetState.valueOf(name);
            else {
                name = jsonObject.getString(K_HS_STATE);
                hs_state = HeadsetState.valueOf(name);
            }
        } else {
                name = jsonObject.getString(K_HS_STATE);
                hs_state = HeadsetState.valueOf(name);
        }
        return hs_state;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (hs_state != null)
            return true;
        if (hs_type != null)
            return true;
        return false;
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof HeadsetUSourceData))
            return false;
        if (!Utils.nullableEqual(hs_state, ((HeadsetUSourceData) obj).hs_state))
            return false;
        if (!Utils.nullableEqual(hs_type, ((HeadsetUSourceData) obj).hs_type))
            return false;
        return true;
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    HeadsetState hs_state = this.hs_state;
                    if (hs_state == HeadsetState.plugged_in)
                        hs_state = HeadsetState.plug_in;
                    if (hs_state == HeadsetState.plugged_out)
                        hs_state = HeadsetState.plug_out;
                    jsonObject.put(K_HS_STATE, hs_state.name());
                    jsonObject.put(K_HS_TYPE, hs_type.name());
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
                res = jsonObject.toString();
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(hs_state);
        dest.writeSerializable(hs_type);
    }

    public static final Creator<HeadsetUSourceData> CREATOR
            = new Creator<HeadsetUSourceData>() {
        public HeadsetUSourceData createFromParcel(Parcel in) {
            return new HeadsetUSourceData(in);
        }

        public HeadsetUSourceData[] newArray(int size) {
            return new HeadsetUSourceData[size];
        }
    };

    private HeadsetUSourceData(Parcel in) {
        hs_state = (HeadsetState) in.readSerializable();
        hs_type = (HeadsetType) in.readSerializable();
    }
}
