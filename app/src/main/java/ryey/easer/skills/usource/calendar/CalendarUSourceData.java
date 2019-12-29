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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ryey.easer.commons.C;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.StorageData;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

/**
 * Merged from event and condition in v0.8
 * TODO: further merging -- move {@link CalendarMatchType} to shared
 * TODO: further merging -- eliminate InnerData
 */
public class CalendarUSourceData implements USourceData {

    interface InnerData extends StorageData {}

    private static final String K_ID = "calendar_id";
    private static final String K_INNER = "inner";

    private static final String T_EVENT = "event";
    private static final String T_CONDITION = "condition";

    final long calendar_id;
    @NonNull
    final InnerData data;

    CalendarUSourceData(long calendar_id, @NonNull InnerData data) {
        this.calendar_id = calendar_id;
        this.data = data;
    }

    CalendarUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        InnerData data1;
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    this.calendar_id = jsonObject.getLong(K_ID);
                    if (version >= C.VERSION_UNIFIED_CALENDAR) {
                        String innerData = jsonObject.getString(K_INNER);
                        try {
                            data1 = new CalConditionInnerData(innerData, format, version);
                        } catch (IllegalStorageDataException e) {
                            data1 = new CalEventInnerData(innerData, format, version);
                        }
                    } else {
                        try {
                            data1 = new CalConditionInnerData(data, format, version);
                        } catch (IllegalStorageDataException e) {
                            data1 = new CalEventInnerData(data, format, version);
                        }
                    }
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
        this.data = data1;
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return null;
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(K_ID, calendar_id);
                    jsonObject.put(K_INNER, data.serialize(format));
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    throw new IllegalAccessError("Serialize shouldn't fail");
                }
        }
        return res;
    }

    @Override
    public boolean isValid() {
        if (calendar_id == -1)
            return false;
        return data.isValid();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof CalendarUSourceData))
            return false;
        if (calendar_id != ((CalendarUSourceData) obj).calendar_id)
            return false;
        return data.equals(((CalendarUSourceData) obj).data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(calendar_id);
        if (data instanceof CalConditionInnerData)
            dest.writeString(T_CONDITION);
        else
            dest.writeString(T_EVENT);
        dest.writeParcelable(data, 0);
    }

    protected CalendarUSourceData(Parcel in) {
        calendar_id = in.readLong();
        switch (Objects.requireNonNull(in.readString())) {
            case T_CONDITION:
                data = Objects.requireNonNull(in.readParcelable(CalConditionInnerData.class.getClassLoader()));
                break;
            case T_EVENT:
                data = Objects.requireNonNull(in.readParcelable(CalEventInnerData.class.getClassLoader()));
                break;
            default:
                throw new IllegalAccessError("This statement should never be reached");
        }

    }

    public static final Creator<CalendarUSourceData> CREATOR = new Creator<CalendarUSourceData>() {
        @Override
        public CalendarUSourceData createFromParcel(Parcel in) {
            return new CalendarUSourceData(in);
        }

        @Override
        public CalendarUSourceData[] newArray(int size) {
            return new CalendarUSourceData[size];
        }
    };
}
