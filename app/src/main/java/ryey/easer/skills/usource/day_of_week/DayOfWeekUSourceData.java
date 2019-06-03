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

package ryey.easer.skills.usource.day_of_week;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Set;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

public class DayOfWeekUSourceData implements USourceData {

    Set<Integer> days = new ArraySet<>(7);

    public DayOfWeekUSourceData(Set<Integer> days) {
        this.days = days;
    }

    DayOfWeekUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
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

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DayOfWeekUSourceData))
            return false;
        if (!days.equals(((DayOfWeekUSourceData) obj).days))
            return false;
        return true;
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        days.clear();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(new ArrayList<>(days));
    }

    public static final Parcelable.Creator<DayOfWeekUSourceData> CREATOR
            = new Parcelable.Creator<DayOfWeekUSourceData>() {
        public DayOfWeekUSourceData createFromParcel(Parcel in) {
            return new DayOfWeekUSourceData(in);
        }

        public DayOfWeekUSourceData[] newArray(int size) {
            return new DayOfWeekUSourceData[size];
        }
    };

    private DayOfWeekUSourceData(Parcel in) {
        ArrayList<Integer> list = new ArrayList<>();
        in.readList(list, null);
        days = new ArraySet<>(list);
    }
}
