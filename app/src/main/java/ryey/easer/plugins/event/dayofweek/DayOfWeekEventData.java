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

package ryey.easer.plugins.event.dayofweek;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Set;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.dynamics.Dynamics;
import ryey.easer.plugins.event.AbstractEventData;

public class DayOfWeekEventData extends AbstractEventData {

    Set<Integer> days = new ArraySet<>(7);

    public DayOfWeekEventData(Set<Integer> days) {
        this.days = days;
    }

    DayOfWeekEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
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
        if (obj == null || !(obj instanceof DayOfWeekEventData))
            return false;
        if (!days.equals(((DayOfWeekEventData) obj).days))
            return false;
        return true;
    }

    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
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
    public String serialize(@NonNull C.Format format) {
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

    public static final Parcelable.Creator<DayOfWeekEventData> CREATOR
            = new Parcelable.Creator<DayOfWeekEventData>() {
        public DayOfWeekEventData createFromParcel(Parcel in) {
            return new DayOfWeekEventData(in);
        }

        public DayOfWeekEventData[] newArray(int size) {
            return new DayOfWeekEventData[size];
        }
    };

    private DayOfWeekEventData(Parcel in) {
        ArrayList<Integer> list = new ArrayList<>();
        in.readList(list, null);
        days = new ArraySet<>(list);
    }
}
