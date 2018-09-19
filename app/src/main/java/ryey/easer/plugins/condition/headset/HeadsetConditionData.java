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

package ryey.easer.plugins.condition.headset;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.Utils;
import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionData;

public class HeadsetConditionData implements ConditionData {

    enum HeadsetState {
        plugged_in,
        plugged_out,
    }

    enum HeadsetType {
        with_microphone,
        without_microphone,
        any,
    }

    private static final String K_HS_STATE = "headset_state";
    private static final String K_HS_TYPE = "headset_type";

    HeadsetState hs_state = HeadsetState.plugged_in;
    HeadsetType hs_type = HeadsetType.any;

    HeadsetConditionData(@Nullable HeadsetState hs_state, @Nullable HeadsetType hs_type) {
        this.hs_state = hs_state;
        this.hs_type = hs_type;
    }

    HeadsetConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                String name;
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    name = jsonObject.optString(K_HS_STATE);
                    if (name != null)
                        hs_state = HeadsetState.valueOf(name);
                    name = jsonObject.optString(K_HS_TYPE);
                    if (name != null)
                        hs_type = HeadsetType.valueOf(name);
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
                    jsonObject.put(K_HS_STATE, hs_state.name());
                    jsonObject.put(K_HS_TYPE, hs_type.name());
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
        if (hs_state != null)
            return true;
        if (hs_type != null)
            return true;
        return false;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof HeadsetConditionData))
            return false;
        if (!Utils.nullableEqual(hs_type, ((HeadsetConditionData) obj).hs_type))
            return false;
        return true;
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

    public static final Creator<HeadsetConditionData> CREATOR
            = new Creator<HeadsetConditionData>() {
        public HeadsetConditionData createFromParcel(Parcel in) {
            return new HeadsetConditionData(in);
        }

        public HeadsetConditionData[] newArray(int size) {
            return new HeadsetConditionData[size];
        }
    };

    private HeadsetConditionData(Parcel in) {
        hs_state = (HeadsetState) in.readSerializable();
        hs_type = (HeadsetType) in.readSerializable();
    }
}
