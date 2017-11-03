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

package ryey.easer.plugins.event.broadcast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.EnumSet;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

public class BroadcastEventData extends TypedEventData {

    static final String K_ACTION = "action";
    static final String K_CATEGORY = "category";

    ReceiverSideIntentData intentData;

    {
        default_type = EventType.is;
        availableTypes = EnumSet.of(EventType.is, EventType.after);
    }

    public BroadcastEventData() {}

    public BroadcastEventData(ReceiverSideIntentData intentData) {
        set(intentData);
    }

    @Override
    public Object get() {
        return intentData;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof ReceiverSideIntentData) {
            intentData = (ReceiverSideIntentData) obj;
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public boolean isValid() {
        if (intentData.action != null && intentData.category != null)
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        throw new IllegalAccessError();
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        throw new IllegalAccessError();
    }

    @Override
    public void parse(String data, C.Format format, int version) throws IllegalStorageDataException {
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
                    throw new IllegalStorageDataException(e.getMessage());
                }
        }
    }

    @Override
    public String serialize(C.Format format) {
        String res = "";
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

}
