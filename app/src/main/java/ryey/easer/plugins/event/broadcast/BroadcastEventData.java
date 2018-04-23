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

package ryey.easer.plugins.event.broadcast;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.plugins.event.AbstractEventData;

public class BroadcastEventData extends AbstractEventData {

    private static final String K_ACTION = "action";
    private static final String K_CATEGORY = "category";

    ReceiverSideIntentData intentData;

    BroadcastEventData(ReceiverSideIntentData intentData) {
        this.intentData = intentData;
    }

    BroadcastEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (intentData.action != null && intentData.category != null)
            return false;
        return true;
    }

    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        intentData = new ReceiverSideIntentData();
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray_action = jsonObject.getJSONArray(K_ACTION);
                    for (int i = 0; i < jsonArray_action.length(); i++) {
                        intentData.action.add(jsonArray_action.getString(i));
                    }
                    JSONArray jsonArray_category = jsonObject.getJSONArray(K_CATEGORY);
                    for (int i = 0; i < jsonArray_category.length(); i++) {
                        intentData.category.add(jsonArray_category.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String res;
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray_action = new JSONArray();
                    if (intentData.action != null) {
                        for (String action : intentData.action) {
                            jsonArray_action.put(action);
                        }
                    }
                    jsonObject.put(K_ACTION, jsonArray_action);
                    JSONArray jsonArray_category = new JSONArray();
                    if (intentData.category != null) {
                        for (String category : intentData.category) {
                            jsonArray_category.put(category);
                        }
                    }
                    jsonObject.put(K_CATEGORY, jsonArray_category);
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
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof BroadcastEventData))
            return false;
        if (!Utils.nullableEqual(intentData.action, ((BroadcastEventData) obj).intentData.action))
            return false;
        if (!Utils.nullableEqual(intentData.category, ((BroadcastEventData) obj).intentData.category))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(intentData.action);
        dest.writeStringList(intentData.category);
    }

    public static final Parcelable.Creator<BroadcastEventData> CREATOR
            = new Parcelable.Creator<BroadcastEventData>() {
        public BroadcastEventData createFromParcel(Parcel in) {
            return new BroadcastEventData(in);
        }

        public BroadcastEventData[] newArray(int size) {
            return new BroadcastEventData[size];
        }
    };

    private BroadcastEventData(Parcel in) {
        intentData = new ReceiverSideIntentData();
        in.readStringList(intentData.action);
        in.readStringList(intentData.category);
    }
}
