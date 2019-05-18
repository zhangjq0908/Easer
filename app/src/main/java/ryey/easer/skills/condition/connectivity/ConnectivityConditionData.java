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

package ryey.easer.skills.condition.connectivity;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Set;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.conditionskill.ConditionData;
import ryey.easer.plugin.PluginDataFormat;

public class ConnectivityConditionData implements ConditionData {

    Set<Integer> connectivity_type = new ArraySet<>();

    ConnectivityConditionData(Set<Integer> connectivity_type) {
        this.connectivity_type = connectivity_type;
    }

    ConnectivityConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
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

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof ConnectivityConditionData))
            return false;
        if (!connectivity_type.equals(((ConnectivityConditionData) obj).connectivity_type))
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

    public static final Creator<ConnectivityConditionData> CREATOR
            = new Creator<ConnectivityConditionData>() {
        public ConnectivityConditionData createFromParcel(Parcel in) {
            return new ConnectivityConditionData(in);
        }

        public ConnectivityConditionData[] newArray(int size) {
            return new ConnectivityConditionData[size];
        }
    };

    private ConnectivityConditionData(Parcel in) {
        ArrayList<Integer> list = new ArrayList<>();
        in.readList(list, null);
        connectivity_type = new ArraySet<>(list);
    }
}
