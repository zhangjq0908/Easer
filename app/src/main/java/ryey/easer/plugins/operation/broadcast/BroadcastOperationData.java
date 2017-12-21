/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.plugins.PluginRegistry;

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

    private IntentData data = new IntentData();

    public BroadcastOperationData() {
    }

    public BroadcastOperationData(IntentData data) {
        this.data = data;
    }

    BroadcastOperationData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @NonNull
    @Override
    public Object get() {
        return data;
    }

    @Override
    public void set(@NonNull Object obj) {
        if (obj instanceof String) {
            data.action = (String) obj;
        } else if (obj instanceof IntentData) {
            data = (IntentData) obj;
        } else {
            throw new IllegalArgumentTypeException(data.getClass(), new Class[]{String.class, IntentData.class});
        }
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        String pname = PluginRegistry.getInstance().operation().findPlugin(this).id();
        int depth = parser.getDepth();
        int event_type = parser.next();
        IntentData intentData = new IntentData();
        while (parser.getDepth() > depth) {
            if (event_type == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case ACTION:
                        if (parser.next() == XmlPullParser.TEXT)
                            intentData.action = parser.getText();
                        else
                            throw new IllegalStorageDataException(String.format("Illegal Item: (%s) Action has No Content", pname));
                        break;
                    case CATEGORY:
                        if (parser.next() == XmlPullParser.TEXT)
                            intentData.category = Utils.stringToStringList(parser.getText());
                        else
                            throw new IllegalStorageDataException(String.format("Illegal Item: (%s) Category is not valid", pname));
                        break;
                    case TYPE:
                        if (parser.next() == XmlPullParser.TEXT)
                            intentData.type = parser.getText();
                        else
                            throw new IllegalStorageDataException(String.format("Illegal Item: (%s) Type is not valid", pname));
                        break;
                    case DATA:
                        if (parser.next() == XmlPullParser.TEXT)
                            intentData.data = Uri.parse(parser.getText());
                        else
                            throw new IllegalStorageDataException(String.format("Illegal Item: (%s) Data is not valid", pname));
                        break;
                    default:
                        XmlHelper.skip(parser);
                }
            }
            event_type = parser.next();
        }
        if (intentData.action == null)
            throw new IllegalStorageDataException(String.format("Illegal Item: (%s) No Action", pname));

        set(intentData);
    }

    /*
     * `isValid()` needs to be called before calling this function.
     */
    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        String pname = PluginRegistry.getInstance().operation().findPlugin(this).id();

        serializer.startTag(ns, C.ITEM);

        serializer.attribute(ns, C.SPEC, pname);

        if (!Utils.isBlank(data.action)) {
            serializer.startTag(ns, ACTION);
            serializer.text(data.action.trim());
            serializer.endTag(ns, ACTION);
        }

        if (data.category != null && !data.category.isEmpty()) {
            serializer.startTag(ns, CATEGORY);
            serializer.text(Utils.StringListToString(data.category));
            serializer.endTag(ns, CATEGORY);
        }

        if (!Utils.isBlank(data.type)) {
            serializer.startTag(ns, TYPE);
            serializer.text(data.type.trim());
            serializer.endTag(ns, TYPE);
        }

        if (data.data != null && !Utils.isBlank(data.data.toString())) {
            serializer.startTag(ns, DATA);
            serializer.text(data.data.toString());
            serializer.endTag(ns, DATA);
        }

        serializer.endTag(ns, C.ITEM);
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
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
                    intentData.data = Uri.parse(jsonObject.optString(DATA, null));

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
                    throw new IllegalStorageDataException(e.getMessage());
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
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
}
