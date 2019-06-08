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

package ryey.easer.skills.usource.cell_location;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

public class CellLocationUSourceData implements USourceData {
    final List<CellLocationSingleData> data = new ArrayList<>();

    CellLocationUSourceData(String[] locations) {
        setFromMultiple(locations);
    }

    CellLocationUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    String[] strings = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        strings[i] = jsonArray.getString(i);
                    }
                    setFromMultiple(strings);
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    private void setFromMultiple(String[] locations) {
        data.clear();
        for (String location : locations) {
            CellLocationSingleData singleData = new CellLocationSingleData();
            singleData.set(location);
            if (singleData.isValid())
                data.add(singleData);
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONArray jsonArray = new JSONArray();
                for (CellLocationSingleData singleData : data) {
                    jsonArray.put(singleData.toString());
                }
                res = jsonArray.toString();
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (data.size() == 0)
            return false;
        return true;
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return new Dynamics[]{new CellLocationDynamics()};
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof CellLocationUSourceData))
            return false;
        if (!data.equals(((CellLocationUSourceData) obj).data))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (data.size() > 0) {
            str.append(data.get(0).toString());
            for (int i = 1; i < data.size(); i++) {
                str.append("\n").append(data.get(i).toString());
            }
        }
        return str.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(data);
    }

    public static final Creator<CellLocationUSourceData> CREATOR
            = new Creator<CellLocationUSourceData>() {
        public CellLocationUSourceData createFromParcel(Parcel in) {
            return new CellLocationUSourceData(in);
        }

        public CellLocationUSourceData[] newArray(int size) {
            return new CellLocationUSourceData[size];
        }
    };

    private CellLocationUSourceData(Parcel in) {
        in.readList(data, CellLocationSingleData.class.getClassLoader());
    }

    static class CellLocationDynamics implements Dynamics {

        static final String id = "ryey.easer.skills.event.cell_location.tower";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.usource_cell_location_dynamics_cell_location;
        }
    }
}
