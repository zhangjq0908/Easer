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

package ryey.easer.plugins.operation.volume;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

public class VolumeOperationData implements OperationData {
    private static final String K_VOL_RING = "ring";
    private static final String K_VOL_MEDIA = "media";
    private static final String K_VOL_ALARM = "alarm";
    private static final String K_VOL_NOTIFICATION = "notification";

    Integer vol_ring;
    Integer vol_media;
    Integer vol_alarm;
    Integer vol_notification;

    public VolumeOperationData() {
    }

    public VolumeOperationData(Integer vol_ring, Integer vol_media, Integer vol_alarm, Integer vol_notification) {
        this.vol_ring = vol_ring;
        this.vol_media = vol_media;
        this.vol_alarm = vol_alarm;
        this.vol_notification = vol_notification;
    }

    VolumeOperationData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    private static Integer optInteger(JSONObject jsonObject, String key) {
        int value = jsonObject.optInt(key, -1);
        if (value == -1)
            return null;
        return value;
    }

    private static void writeNonNull(JSONObject jsonObject, Integer value, String key) throws JSONException {
        if (value != null)
            jsonObject.put(key, value);
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    vol_ring = optInteger(jsonObject, K_VOL_RING);
                    vol_media = optInteger(jsonObject, K_VOL_MEDIA);
                    vol_alarm = optInteger(jsonObject, K_VOL_ALARM);
                    vol_notification = optInteger(jsonObject, K_VOL_NOTIFICATION);
                } catch (JSONException e) {
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
                JSONObject jsonObject = new JSONObject();
                try {
                    writeNonNull(jsonObject, vol_ring, K_VOL_RING);
                    writeNonNull(jsonObject, vol_media, K_VOL_MEDIA);
                    writeNonNull(jsonObject, vol_alarm, K_VOL_ALARM);
                    writeNonNull(jsonObject, vol_notification, K_VOL_NOTIFICATION);
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof VolumeOperationData))
            return false;
        if (!Utils.nullableEqual(vol_ring, ((VolumeOperationData) obj).vol_ring))
            return false;
        if (!Utils.nullableEqual(vol_media, ((VolumeOperationData) obj).vol_media))
            return false;
        if (!Utils.nullableEqual(vol_alarm, ((VolumeOperationData) obj).vol_alarm))
            return false;
        if (!Utils.nullableEqual(vol_notification, ((VolumeOperationData) obj).vol_notification))
            return false;
        return true;
    }

    @Override
    public boolean isValid() {
        return isNotNegative(vol_ring)
                || isNotNegative(vol_media)
                || isNotNegative(vol_alarm)
                || isNotNegative(vol_notification);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(vol_ring);
        parcel.writeValue(vol_media);
        parcel.writeValue(vol_alarm);
        parcel.writeValue(vol_notification);
    }

    public static final Creator<VolumeOperationData> CREATOR
            = new Creator<VolumeOperationData>() {
        public VolumeOperationData createFromParcel(Parcel in) {
            return new VolumeOperationData(in);
        }

        public VolumeOperationData[] newArray(int size) {
            return new VolumeOperationData[size];
        }
    };

    private VolumeOperationData(Parcel in) {
        vol_ring = (Integer) in.readValue(Integer.class.getClassLoader());
        vol_media = (Integer) in.readValue(Integer.class.getClassLoader());
        vol_alarm = (Integer) in.readValue(Integer.class.getClassLoader());
        vol_notification = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    private static boolean isNotNegative(@Nullable Integer value) {
        return value != null && value >= 0;
    }
}
