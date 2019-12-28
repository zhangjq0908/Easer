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

package ryey.easer.skills.usource.calendar;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.plugin.PluginDataFormat;


public class CalConditionInnerData implements CalendarUSourceData.InnerData {

    private static final String T_match_type = "match_type";
    private static final String T_match_pattern = "match_pattern";
    private static final String T_all_day = "all_day";

    @NonNull
    final CalendarMatchType matchType;
    @NonNull
    final String matchPattern;
    final boolean isAllDayEvent;

    CalConditionInnerData(@NonNull CalendarMatchType matchType, @NonNull String matchPattern, boolean isAllDayEvent) {
        this.matchType = matchType;
        this.matchPattern = matchPattern;
        this.isAllDayEvent = isAllDayEvent;
    }

    CalConditionInnerData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            matchType = CalendarMatchType.getById(jsonObject.optInt(T_match_type, 0));
            matchPattern = jsonObject.optString(T_match_pattern, "*");
            isAllDayEvent = jsonObject.getBoolean(T_all_day);
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e);
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
                    jsonObject.put(T_match_type, matchType.getId());
                    jsonObject.put(T_match_pattern, matchPattern);
                    jsonObject.put(T_all_day, isAllDayEvent);
                } catch (JSONException e) {
                    throw new IllegalAccessError("serialize shouldn't fail");
                }
                res = jsonObject.toString();
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof CalConditionInnerData))
            return false;
        if (isAllDayEvent != ((CalConditionInnerData) obj).isAllDayEvent
                || matchType != ((CalConditionInnerData) obj).matchType) {
            return false;
        }
        if (matchType == CalendarMatchType.EVENT_TITLE && !(matchPattern.equals(((CalConditionInnerData) obj).matchPattern))) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(matchType.getId());
        dest.writeString(matchPattern);
        dest.writeByte((byte) (isAllDayEvent ? 1 : 0));
    }

    public static final Creator<CalConditionInnerData> CREATOR
            = new Creator<CalConditionInnerData>() {
        public CalConditionInnerData createFromParcel(Parcel in) {
            return new CalConditionInnerData(in);
        }

        public CalConditionInnerData[] newArray(int size) {
            return new CalConditionInnerData[size];
        }
    };

    private CalConditionInnerData(Parcel in) {
        matchType = CalendarMatchType.getById(in.readInt());
        matchPattern = Objects.requireNonNull(in.readString());
        isAllDayEvent = in.readByte() != 0;
    }
}
