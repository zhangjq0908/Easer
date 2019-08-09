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

package ryey.easer.skills.usource.call;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

public class CallUSourceData implements USourceData {

//    enum Direction {
//        incoming,
//        outgoing,
//        any,
//    }

    enum CallState {
        IDLE,
        RINGING,
        OFFHOOK,
    }

//    private static final String K_DIRECTION = "direction";
    private static final String K_STATE = "state";
    private static final String K_NUMBER = "number";

//    @NonNull
//    final Direction direction;

    @NonNull
    final ArrayList<CallState> callStates = new ArrayList<>();

    @Nullable
    final String number;

//    CallUSourceData(@NonNull Direction direction, @NonNull List<CallState> callStates, @Nullable String number) {
//        this.direction = direction;
//        this.callStates.addAll(callStates);
//        if (Utils.isBlank(number)) {
//            this.number = null;
//        } else {
//            this.number = number;
//        }
//    }
    CallUSourceData(@NonNull List<CallState> callStates, @Nullable String number) {
        this.callStates.addAll(callStates);
        if (Utils.isBlank(number)) {
            this.number = null;
        } else {
            this.number = number;
        }
    }

    CallUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
//                    direction = Direction.valueOf(jsonObject.getString(K_DIRECTION));
                    if (jsonObject.has(K_STATE)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(K_STATE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            callStates.add(CallState.valueOf(jsonArray.getString(i)));
                        }
                    }
                    number = jsonObject.optString(K_NUMBER, null);
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
                try {
                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put(K_DIRECTION, direction);
                    if (callStates.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (CallState state : callStates) {
                            jsonArray.put(state.toString());
                        }
                        jsonObject.put(K_STATE, jsonArray);
                    }
                    if (Utils.isBlank(number)) {
                        jsonObject.put(K_NUMBER, number);
                    }
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
//        if (direction == null)
//            return false;
        return true;
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof CallUSourceData))
            return false;
//        if (!((CallUSourceData) obj).direction.equals(direction))
//            return false;
        if (!((CallUSourceData) obj).callStates.equals(callStates))
            return false;
        if (!Utils.nullableEqual(((CallUSourceData) obj).number, number))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(direction.ordinal());
        dest.writeList(callStates); //TODO: create generic util function and replace all List<Enum>
        dest.writeString(number);
    }

    public static final Creator<CallUSourceData> CREATOR
            = new Creator<CallUSourceData>() {
        public CallUSourceData createFromParcel(Parcel in) {
            return new CallUSourceData(in);
        }

        public CallUSourceData[] newArray(int size) {
            return new CallUSourceData[size];
        }
    };

    private CallUSourceData(Parcel in) {
//        direction = Direction.values()[in.readInt()];
        in.readList(callStates, null);
        number = in.readString();
    }
}
