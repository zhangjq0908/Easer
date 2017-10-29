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

package ryey.easer.plugins.event.calendar;

import com.orhanobut.logger.Logger;

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
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.event.TypedEventData;

public class CalendarEventData extends TypedEventData {

    private static final String T_calendar_id = "calendar_id";
    private static final String T_condition = "condition";

    CalendarData data;

    {
        default_type = EventType.is;
        availableTypes = EnumSet.of(EventType.is);
    }

    public CalendarEventData() {}

    public CalendarEventData(CalendarData data) {
        set(data);
    }

    @Override
    public Object get() {
        return data;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof CalendarData) {
            this.data = (CalendarData) obj;
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public boolean isValid() {
        if (data == null)
            return false;
        if (data.calendar_id == -1)
            return false;
        boolean any_true = false;
        for (Boolean v : data.conditions.values()) {
            if (v) {
                any_true = true;
                break;
            }
        }
        if (!any_true)
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalXmlException {
        String res_str = XmlHelper.EventHelper.readSingleSituation(parser);
        try {
            JSONObject jsonObject = new JSONObject(res_str);
            this.data = new CalendarData();
            this.data.calendar_id = jsonObject.optLong(T_calendar_id);
            JSONArray jsonArray_conditions = jsonObject.optJSONArray(T_condition);
            for (int i = 0; i < jsonArray_conditions.length(); i++) {
                String condition = jsonArray_conditions.getString(i);
                for (int j = 0; j < CalendarData.condition_name.length; j++) {
                    this.data.conditions.put(condition, true);
                }
            }
        } catch (JSONException e) {
            Logger.e(e, "Error parsing %s data to JSON", getClass().getSimpleName());
            e.printStackTrace();
        }

        EventType type = XmlHelper.EventHelper.readLogic(parser);
        setType(type);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        if (!isValid()) {
            Logger.wtf("Invalid CalendarEventData shouldn't be serialized");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(T_calendar_id, data.calendar_id);
            JSONArray jsonArray_conditions = new JSONArray();
            for (String k : data.conditions.keySet()) {
                if (data.conditions.get(k)) {
                    jsonArray_conditions.put(k);
                }
            }
            jsonObject.put(T_condition, jsonArray_conditions);
        } catch (JSONException e) {
            Logger.e(e, "Error putting %s data", getClass().getSimpleName());
            e.printStackTrace();
        }
        XmlHelper.EventHelper.writeSingleSituation(serializer, PluginRegistry.getInstance().event().findPlugin(this).name(), jsonObject.toString());
        XmlHelper.EventHelper.writeLogic(serializer, type());
    }

    @Override
    public void parse(String data, C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    this.data = new CalendarData();
                    this.data.calendar_id = jsonObject.optLong(T_calendar_id);
                    JSONArray jsonArray_conditions = jsonObject.optJSONArray(T_condition);
                    for (int i = 0; i < jsonArray_conditions.length(); i++) {
                        String condition = jsonArray_conditions.getString(i);
                        for (int j = 0; j < CalendarData.condition_name.length; j++) {
                            this.data.conditions.put(condition, true);
                        }
                    }
                } catch (JSONException e) {
                    Logger.e(e, "Error parsing %s data to JSON", getClass().getSimpleName());
                    e.printStackTrace();
                }
        }
    }

    @Override
    public String serialize(C.Format format) {
        String res = "";
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(T_calendar_id, data.calendar_id);
                    JSONArray jsonArray_conditions = new JSONArray();
                    for (String k : data.conditions.keySet()) {
                        if (data.conditions.get(k)) {
                            jsonArray_conditions.put(k);
                        }
                    }
                    jsonObject.put(T_condition, jsonArray_conditions);
                } catch (JSONException e) {
                    Logger.e(e, "Error putting %s data", getClass().getSimpleName());
                    e.printStackTrace();
                }
                res = jsonObject.toString();
        }
        return res;
    }
}
