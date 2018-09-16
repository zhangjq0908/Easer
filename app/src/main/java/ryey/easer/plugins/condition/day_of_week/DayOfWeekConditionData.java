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

package ryey.easer.plugins.condition.day_of_week;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Set;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.plugindef.conditionplugin.ConditionData;

public class DayOfWeekConditionData implements ConditionData {

    Set<Integer> days = new ArraySet<>(7);

    DayOfWeekConditionData(Set<Integer> days) {
        this.days = days;
    }

    DayOfWeekConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        days.add(jsonArray.getInt(i));
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
                JSONArray jsonArray = new JSONArray();
                for (Integer v : days) {
                    jsonArray.put(v);
                }
                res = jsonArray.toString();
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (days == null)
            return false;
        if (days.isEmpty())
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof DayOfWeekConditionData))
            return false;
        if (!days.equals(((DayOfWeekConditionData) obj).days))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(new ArrayList<>(days));
    }

    public static final Creator<DayOfWeekConditionData> CREATOR
            = new Creator<DayOfWeekConditionData>() {
        public DayOfWeekConditionData createFromParcel(Parcel in) {
            return new DayOfWeekConditionData(in);
        }

        public DayOfWeekConditionData[] newArray(int size) {
            return new DayOfWeekConditionData[size];
        }
    };

    private DayOfWeekConditionData(Parcel in) {
        ArrayList<Integer> list = new ArrayList<>();
        in.readList(list, null);
        days = new ArraySet<>(list);
    }
}
