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

package ryey.easer.skills.usource.connectivity;

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

public class ConnectivityEventData implements USourceData {

    Set<Integer> connectivity_type = new ArraySet<>();

    public ConnectivityEventData(Set<Integer> connectivity_type) {
        this.connectivity_type = connectivity_type;
    }

    ConnectivityEventData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        connectivity_type.clear();
        switch (format) {
            default:
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        connectivity_type.add(jsonArray.getInt(i));
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
                for (Integer v : connectivity_type) {
                    jsonArray.put(v);
                }
                res = jsonArray.toString();
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (connectivity_type.size() > 0)
            return true;
        return false;
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ConnectivityEventData))
            return false;
        if (!connectivity_type.equals(((ConnectivityEventData) obj).connectivity_type))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(new ArrayList<>(connectivity_type));
    }

    public static final Parcelable.Creator<ConnectivityEventData> CREATOR
            = new Parcelable.Creator<ConnectivityEventData>() {
        public ConnectivityEventData createFromParcel(Parcel in) {
            return new ConnectivityEventData(in);
        }

        public ConnectivityEventData[] newArray(int size) {
            return new ConnectivityEventData[size];
        }
    };

    private ConnectivityEventData(Parcel in) {
        ArrayList<Integer> list = new ArrayList<>();
        in.readList(list, null);
        connectivity_type = new ArraySet<>(list);
    }
}
