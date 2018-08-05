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

package ryey.easer.plugins.operation.broadcast;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

public class BroadcastOperationData implements OperationData {
    private static final String ns = null;

    private static final String ACTION = "action";
    private static final String CATEGORY = "category";
    private static final String TYPE = "type";
    private static final String DATA = "data";
    private static final String EXTRAS = "extras";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String V_TYPE = "type";

    IntentData data = new IntentData();

    BroadcastOperationData(IntentData data) {
        this.data = data;
    }

    BroadcastOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    IntentData intentData = new IntentData();
                    intentData.action = jsonObject.optString(ACTION, null);

                    JSONArray jsonArray = jsonObject.optJSONArray(CATEGORY);
                    if ((jsonArray != null) && (jsonArray.length() > 0)) {
                        intentData.category = new ArrayList<>(jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            intentData.category.add(jsonArray.getString(i));
                        }
                    }

                    intentData.type = jsonObject.optString(TYPE, null);

                    String uri = jsonObject.optString(DATA, null);
                    if (uri != null)
                        intentData.data = Uri.parse(uri);

                    JSONArray jsonArray_extras = jsonObject.optJSONArray(EXTRAS);
                    if (jsonArray_extras != null) {
                        intentData.extras = new ArrayList<>(jsonArray_extras.length());
                        for (int i = 0; i < jsonArray_extras.length(); i++) {
                            JSONObject jsonObject_extra = jsonArray_extras.getJSONObject(i);
                            IntentData.ExtraItem item = new IntentData.ExtraItem();
                            item.key = jsonObject_extra.getString(KEY);
                            item.value = jsonObject_extra.getString(VALUE);
                            item.type = jsonObject_extra.getString(V_TYPE);
                            intentData.extras.add(item);
                        }
                    }

                    this.data = intentData;
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res = "";
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(ACTION, data.action);

                    if (data.category != null && data.category.size() > 0) {
                        JSONArray jsonArray_category = new JSONArray();
                        for (String category : data.category) {
                            jsonArray_category.put(category);
                        }
                        jsonObject.put(CATEGORY, jsonArray_category);
                    }

                    if (!Utils.isBlank(data.type))
                        jsonObject.put(TYPE, data.type);
                    if (data.data != null)
                        jsonObject.put(DATA, data.data.toString());

                    if (data.extras != null && data.extras.size() > 0) {
                        JSONArray jsonArray_extras = new JSONArray();
                        for (IntentData.ExtraItem item : data.extras) {
                            JSONObject jsonObject_extra = new JSONObject();
                            jsonObject_extra.put(KEY, item.key);
                            jsonObject_extra.put(VALUE, item.value);
                            jsonObject_extra.put(V_TYPE, item.type);
                            jsonArray_extras.put(jsonObject_extra);
                        }
                        jsonObject.put(EXTRAS, jsonArray_extras);
                    }

                    res = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (!Utils.isBlank(data.action))
            return true;
        if (data.category != null && !data.category.isEmpty())
            return true;
        if (!Utils.isBlank(data.type))
            return true;
        if (data.data != null && !Utils.isBlank(data.data.toString()))
            return true;
        return false;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof BroadcastOperationData))
            return false;
        return data.equals(((BroadcastOperationData) obj).data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, 0);
    }

    public static final Parcelable.Creator<BroadcastOperationData> CREATOR
            = new Parcelable.Creator<BroadcastOperationData>() {
        public BroadcastOperationData createFromParcel(Parcel in) {
            return new BroadcastOperationData(in);
        }

        public BroadcastOperationData[] newArray(int size) {
            return new BroadcastOperationData[size];
        }
    };

    private BroadcastOperationData(Parcel in) {
        data = in.readParcelable(IntentData.class.getClassLoader());
    }

    @Nullable
    @Override
    public Set<String> placeholders() {
        Set<String> placeholders = new ArraySet<>();
        if (data.action != null)
            placeholders.addAll(Utils.extractPlaceholder(data.action));
        if (data.category != null) {
            for (String category : data.category)
                placeholders.addAll(Utils.extractPlaceholder(category));
        }
        if (data.type != null)
            placeholders.addAll(Utils.extractPlaceholder(data.type));
        if (data.data != null)
            placeholders.addAll(Utils.extractPlaceholder(data.data.getPath()));
        if (data.extras != null) {
            for (IntentData.ExtraItem extra : data.extras) {
                placeholders.addAll(Utils.extractPlaceholder(extra.key));
                placeholders.addAll(Utils.extractPlaceholder(extra.value));
            }
        }
        return placeholders;
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        IntentData intentData = new IntentData();
        if (data.action != null)
            intentData.action = Utils.applyDynamics(data.action, dynamicsAssignment);
        if (data.category != null) {
            intentData.category = new ArrayList<>(data.category.size());
            for (String category : data.category)
                intentData.category.add(Utils.applyDynamics(category, dynamicsAssignment));
        }
        if (data.type != null)
            intentData.type = Utils.applyDynamics(data.type, dynamicsAssignment);
        if (data.data != null)
            intentData.data = Uri.parse(Utils.applyDynamics(data.data.getPath(), dynamicsAssignment));
        if (data.extras != null) {
            intentData.extras = new ArrayList<>();
            for (IntentData.ExtraItem extra : data.extras) {
                IntentData.ExtraItem p_extra = new IntentData.ExtraItem();
                p_extra.type = extra.type;
                p_extra.key = Utils.applyDynamics(extra.key, dynamicsAssignment);
                p_extra.value = Utils.applyDynamics(extra.value, dynamicsAssignment);
                intentData.extras.add(p_extra);
            }
        }
        return new BroadcastOperationData(intentData);
    }
}
