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

package ryey.easer.plugins.event.celllocation;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.commons.local_plugin.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.plugins.event.AbstractEventData;

public class CellLocationEventData extends AbstractEventData {
    private List<CellLocationSingleData> data = new ArrayList<>();

    public CellLocationEventData() {
        //FIXME
    }

    CellLocationEventData(String[] locations) {
        setFromMultiple(locations);
    }

    public static CellLocationEventData fromString(String repr) {
        CellLocationEventData cellLocationEventData = new CellLocationEventData(repr.split("\n"));
        if (cellLocationEventData.isValid())
            return cellLocationEventData;
        else
            return null;
    }

    CellLocationEventData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
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
        return null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CellLocationEventData))
            return false;
        if (!data.equals(((CellLocationEventData) obj).data))
            return false;
        return true;
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
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

    public boolean add(CellLocationSingleData singleData) {
        if (contains(singleData))
            return false;
        data.add(singleData);
        return true;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        if (data.size() > 0) {
            str.append(data.get(0).toString());
            for (int i = 1; i < data.size(); i++) {
                CellLocationSingleData singleData = data.get(i);
                str.append("\n").append(singleData.toString());
            }
        }
        return str.toString();
    }

    boolean contains(CellLocationSingleData singleData) {
        for (CellLocationSingleData singleData1 : data) {
            if (singleData.equals(singleData1))
                return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(data);
    }

    public static final Parcelable.Creator<CellLocationEventData> CREATOR
            = new Parcelable.Creator<CellLocationEventData>() {
        public CellLocationEventData createFromParcel(Parcel in) {
            return new CellLocationEventData(in);
        }

        public CellLocationEventData[] newArray(int size) {
            return new CellLocationEventData[size];
        }
    };

    private CellLocationEventData(Parcel in) {
        in.readList(data, CellLocationSingleData.class.getClassLoader());
    }
}
