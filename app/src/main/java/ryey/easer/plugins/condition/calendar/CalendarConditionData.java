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

package ryey.easer.plugins.condition.calendar;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionData;
import ryey.easer.plugin.PluginDataFormat;


public class CalendarConditionData implements ConditionData {

    private static final String T_calendar_id = "calendar_id";
    private static final String T_match_type = "match_type";
    private static final String T_match_pattern = "match_pattern";
    private static final String T_all_day = "all_day";

    final CalendarData data;

    CalendarConditionData(CalendarData data) {
        this.data = data;
    }

    CalendarConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        long calendar_id = -1;
        CalendarConditionMatchType matchType = CalendarConditionMatchType.ANY;
        String matchPattern = "%";
        boolean isAllDayEvent = false;
        try {
            JSONObject jsonObject = new JSONObject(data);
            calendar_id = jsonObject.optLong(T_calendar_id);
            matchType = CalendarConditionMatchType.getById(jsonObject.optInt(T_match_type, 0));
            matchPattern = jsonObject.optString(T_match_pattern, "*");
            isAllDayEvent = jsonObject.getBoolean(T_all_day);
        } catch (JSONException e) {
            Logger.e(e, "Error parsing %s data to SUFFIX", getClass().getSimpleName());
            e.printStackTrace();
        }
        this.data = new CalendarData(calendar_id, matchType, matchPattern, isAllDayEvent);
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(T_calendar_id, data.calendar_id);
                    jsonObject.put(T_match_type, data.matchType.getId());
                    jsonObject.put(T_match_pattern, data.matchPattern);
                    jsonObject.put(T_all_day, data.isAllDayEvent);
                } catch (JSONException e) {
                    Logger.e(e, "Error putting %s data", getClass().getSimpleName());
                    e.printStackTrace();
                }
                res = jsonObject.toString();
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (data == null || !(data instanceof CalendarData))
            return false;
        if (data.calendar_id == -1)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof CalendarConditionData))
            return false;
        if (!data.equals(((CalendarConditionData)obj).data))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(data);
    }

    public static final Creator<CalendarConditionData> CREATOR
            = new Creator<CalendarConditionData>() {
        public CalendarConditionData createFromParcel(Parcel in) {
            return new CalendarConditionData(in);
        }

        public CalendarConditionData[] newArray(int size) {
            return new CalendarConditionData[size];
        }
    };

    private CalendarConditionData(Parcel in) {
        data = (CalendarData) in.readValue(getClass().getClassLoader());
    }
}
