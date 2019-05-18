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

package ryey.easer.skills.event.headset;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.event.AbstractEventData;

public class HeadsetEventData extends AbstractEventData {

    enum HeadsetAction {
        plug_in,
        plug_out,
        any,
    }

    enum HeadsetType {
        with_microphone,
        without_microphone,
        any,
    }

    private static final String K_HS_ACTION = "headset_action";
    private static final String K_HS_TYPE = "headset_type";

    HeadsetAction hs_action = HeadsetAction.any;
    HeadsetType hs_type = HeadsetType.any;

    HeadsetEventData(@Nullable HeadsetAction hs_action, @Nullable HeadsetType hs_type) {
        this.hs_action = hs_action;
        this.hs_type = hs_type;
    }

    HeadsetEventData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (hs_action != null)
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
        if (obj == null || !(obj instanceof HeadsetEventData))
            return false;
        if (!Utils.nullableEqual(hs_action, ((HeadsetEventData) obj).hs_action))
            return false;
        if (!Utils.nullableEqual(hs_type, ((HeadsetEventData) obj).hs_type))
            return false;
        return true;
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                String name;
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    name = jsonObject.optString(K_HS_ACTION);
                    if (name != null)
                        hs_action = HeadsetAction.valueOf(name);
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
                    jsonObject.put(K_HS_ACTION, hs_action.name());
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
        dest.writeSerializable(hs_action);
        dest.writeSerializable(hs_type);
    }

    public static final Creator<HeadsetEventData> CREATOR
            = new Creator<HeadsetEventData>() {
        public HeadsetEventData createFromParcel(Parcel in) {
            return new HeadsetEventData(in);
        }

        public HeadsetEventData[] newArray(int size) {
            return new HeadsetEventData[size];
        }
    };

    private HeadsetEventData(Parcel in) {
        hs_action = (HeadsetAction) in.readSerializable();
        hs_type = (HeadsetType) in.readSerializable();
    }
}
