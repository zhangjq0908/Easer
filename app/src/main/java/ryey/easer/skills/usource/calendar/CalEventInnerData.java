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
import androidx.collection.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;

public class CalEventInnerData implements CalendarUSourceData.InnerData {
    private static final String T_condition = "condition";

    static final String[] condition_name = new String[]{
            "start",
            "end",
    };

    @NonNull final Set<String> conditions;

    CalEventInnerData(@Nullable Set<String> conditions) {
        if (conditions == null)
            conditions = new ArraySet<>(condition_name.length);
        this.conditions = conditions;
    }

    CalEventInnerData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    this.conditions = new ArraySet<>();
                    JSONArray jsonArray_conditions = jsonObject.optJSONArray(T_condition);
                    for (int i = 0; i < jsonArray_conditions.length(); i++) {
                        String condition = jsonArray_conditions.getString(i);
                        for (int j = 0; j < condition_name.length; j++) {
                            this.conditions.add(condition);
                        }
                    }
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (conditions.size() == 0)
            return false;
        return true;
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    JSONArray jsonArray_conditions = new JSONArray();
                    for (String k : conditions) {
                        jsonArray_conditions.put(k);
                    }
                    jsonObject.put(T_condition, jsonArray_conditions);
                } catch (JSONException e) {
                    throw new IllegalAccessError("CalEventInnerData serialize shouldn't fail");
                }
                res = jsonObject.toString();
        }
        return res;
    }

    @Nullable
    public Dynamics[] dynamics() {
        return null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof CalEventInnerData))
            return false;
        if (!conditions.equals(((CalEventInnerData) obj).conditions))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(new ArrayList<>(conditions));
    }

    public static final Creator<CalEventInnerData> CREATOR
            = new Creator<CalEventInnerData>() {
        public CalEventInnerData createFromParcel(Parcel in) {
            return new CalEventInnerData(in);
        }

        public CalEventInnerData[] newArray(int size) {
            return new CalEventInnerData[size];
        }
    };

    private CalEventInnerData(Parcel in) {
        ArrayList<String> list = new ArrayList<>();
        in.readList(list, null);
        conditions = new ArraySet<>(list);
    }
}
