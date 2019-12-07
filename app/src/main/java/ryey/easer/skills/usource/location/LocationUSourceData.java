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

package ryey.easer.skills.usource.location;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

public class LocationUSourceData implements USourceData {

    static final int UNIT_LISTEN_MIN_TIME = 1000; // 1s = 1000ms
    static final int UNIT_THRESHOLD_TIME = 1000; // 1s = 1000ms

    private static final String K_LOCATION = "location";
    private static final String K_RADIUS = "radius";
    private static final String K_LISTEN_MIN_TIME = "listen_min_time";
    private static final String K_THRESHOLD_AGE = "threshold_age";
    private static final String K_THRESHOLD_ACCURACY = "threshold_accuracy";

    final ArraySet<LatLong> locations;
    final int radius;
    final long listenMinTime;
    final long thresholdAge;
    final int thresholdAccuracy;

    public LocationUSourceData(ArraySet<LatLong> locations, int radius, long listenMinTime, long thresholdAge, int thresholdAccuracy) {
        this.locations = locations;
        this.radius = radius;
        this.listenMinTime = listenMinTime;
        this.thresholdAge = thresholdAge;
        this.thresholdAccuracy = thresholdAccuracy;
    }

    LocationUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    {
                        locations = new ArraySet<>();
                        JSONArray arrayLocation = jsonObject.getJSONArray(K_LOCATION);
                        for (int i = 0; i < arrayLocation.length(); i++) {
                            JSONArray latLong = arrayLocation.getJSONArray(i);
                            locations.add(new LatLong(latLong.getDouble(0), latLong.getDouble(1)));
                        }
                    }
                    radius = jsonObject.getInt(K_RADIUS);
                    listenMinTime = jsonObject.getLong(K_LISTEN_MIN_TIME);
                    thresholdAge = jsonObject.getLong(K_THRESHOLD_AGE);
                    thresholdAccuracy = jsonObject.getInt(K_THRESHOLD_ACCURACY);
                } catch (JSONException e) {
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
                try {
                    JSONObject jsonObject = new JSONObject();
                    {
                        JSONArray arrayLocation = new JSONArray();
                        for (LatLong loc : locations) {
                            JSONArray latLong = new JSONArray();
                            latLong.put(loc.latitude).put(loc.longitude);
                            arrayLocation.put(latLong);
                        }
                        jsonObject.put(K_LOCATION, arrayLocation);
                    }
                    jsonObject.put(K_RADIUS, radius);
                    jsonObject.put(K_LISTEN_MIN_TIME, listenMinTime);
                    jsonObject.put(K_THRESHOLD_AGE, thresholdAge);
                    jsonObject.put(K_THRESHOLD_ACCURACY, thresholdAccuracy);
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (locations.size() == 0)
            return false;
        if (radius <= 0)
            return false;
        if (listenMinTime <= 0)
            return false;
        if (thresholdAge < 0)
            return false;
        if (thresholdAccuracy < 0)
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
        if (obj == this)
            return true;
        if (!(obj instanceof LocationUSourceData))
            return false;
        if (!locations.equals(((LocationUSourceData) obj).locations))
            return false;
        if (radius != ((LocationUSourceData) obj).radius)
            return false;
        if (listenMinTime != ((LocationUSourceData) obj).listenMinTime)
            return false;
        if (thresholdAge != ((LocationUSourceData) obj).thresholdAge)
            return false;
        if (thresholdAccuracy != ((LocationUSourceData) obj).thresholdAccuracy)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(locations.toArray(new LatLong[0]), 0);
        dest.writeInt(radius);
        dest.writeLong(listenMinTime);
        dest.writeLong(thresholdAge);
        dest.writeLong(thresholdAccuracy);
    }

    public static final Creator<LocationUSourceData> CREATOR
            = new Creator<LocationUSourceData>() {
        public LocationUSourceData createFromParcel(Parcel in) {
            return new LocationUSourceData(in);
        }

        public LocationUSourceData[] newArray(int size) {
            return new LocationUSourceData[size];
        }
    };

    private LocationUSourceData(Parcel in) {
        locations = new ArraySet<>(Arrays.asList(Objects.requireNonNull(in.createTypedArray(LatLong.CREATOR))));
        radius = in.readInt();
        listenMinTime = in.readLong();
        thresholdAge = in.readLong();
        thresholdAccuracy = in.readInt();
    }
}
