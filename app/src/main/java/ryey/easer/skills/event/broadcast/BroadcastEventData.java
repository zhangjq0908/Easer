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

package ryey.easer.skills.event.broadcast;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.event.AbstractEventData;
import ryey.easer.skills.reusable.Extras;

public class BroadcastEventData extends AbstractEventData {

    private static final String K_ACTION = "action";
    private static final String K_CATEGORY = "category";
    private static final String K_EXTRAS = "extras";

    ReceiverSideIntentData intentData;

    BroadcastEventData(ReceiverSideIntentData intentData) {
        this.intentData = intentData;
    }

    BroadcastEventData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if ((intentData.action == null || intentData.action.size() == 0) &&
                (intentData.category == null || intentData.category.size() == 0))
            return false;
        return true;
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
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
                    String strExtras = jsonObject.optString(K_EXTRAS);
                    if (strExtras != null) {
                        intentData.extras = Extras.mayParse(strExtras, format, version);
                    }
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
                    if (intentData.extras != null) {
                        if (intentData.extras.extras.size() > 0) { // Safety check, because old versions may serialise null extras. Should be removed in future.
                            jsonObject.put(K_EXTRAS, intentData.extras.serialize(format));
                        }
                    }
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStateException(e.getMessage());
                }
        }
        return res;
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return new Dynamics[]{new ActionDynamics(), new CategoryDynamics(), new TypeDynamics(), new DataDynamics()};
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
        if (!Utils.nullableEqual(intentData.extras, ((BroadcastEventData) obj).intentData.extras))
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
        dest.writeParcelable(intentData.extras, 0);
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
        intentData.extras = in.readParcelable(Extras.class.getClassLoader());
    }

    static class ActionDynamics implements Dynamics {

        static final String id = "ryey.easer.skills.event.broadcast.action";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.broadcast_action;
        }
    }

    static class CategoryDynamics implements Dynamics {

        static final String id = "ryey.easer.skills.event.broadcast.category";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.broadcast_category;
        }
    }

    static class TypeDynamics implements Dynamics {

        static final String id = "ryey.easer.skills.event.broadcast.type";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.broadcast_type;
        }
    }

    static class DataDynamics implements Dynamics {

        static final String id = "ryey.easer.skills.event.broadcast.data";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.broadcast_data;
        }
    }
}
