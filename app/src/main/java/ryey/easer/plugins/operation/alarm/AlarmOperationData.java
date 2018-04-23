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

package ryey.easer.plugins.operation.alarm;

import android.os.Parcel;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

public class AlarmOperationData implements OperationData {
    private static final String K_TIME = "time";
    private static final String K_MESSAGE = "message";
    private static final String K_ABSOLUTE_BOOL = "absolute?";

    private static final int[] TIME_FIELDS = new int[]{Calendar.HOUR_OF_DAY, Calendar.MINUTE};

    private static final SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm", Locale.US);

    static String TimeToText(Calendar calendar) {
        return sdf_time.format(calendar.getTime());
    }

    static Calendar TextToTime(String text) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf_time.parse(text));
        return calendar;
    }

    Calendar time;
    String message;
    boolean absolute = true;

    AlarmOperationData(Calendar time, String message, boolean absolute) {
        this.time = time;
        this.message = message;
        this.absolute = absolute;
    }

    AlarmOperationData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    try {
                        time = TextToTime(jsonObject.getString(K_TIME));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new IllegalStorageDataException(e);
                    }
                    message = jsonObject.optString(K_MESSAGE, null);
                    absolute = jsonObject.optBoolean(K_ABSOLUTE_BOOL, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String res = "";
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(K_TIME, TimeToText(time));
                    jsonObject.put(K_MESSAGE, message);
                    jsonObject.put(K_ABSOLUTE_BOOL, absolute);
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (time == null)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof AlarmOperationData))
            return false;
        if (!Utils.nullableEqual(message, ((AlarmOperationData) obj).message))
            return false;
        for (int field : TIME_FIELDS)
            if (time.get(field) != ((AlarmOperationData) obj).time.get(field))
                return false;
        if (absolute != ((AlarmOperationData) obj).absolute)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        for (int field : TIME_FIELDS) {
            dest.writeInt(time.get(field));
        }
        dest.writeString(message);
        dest.writeByte((byte) (absolute ? 1 : 0));
    }

    public static final Creator<AlarmOperationData> CREATOR
            = new Creator<AlarmOperationData>() {
        public AlarmOperationData createFromParcel(Parcel in) {
            return new AlarmOperationData(in);
        }

        public AlarmOperationData[] newArray(int size) {
            return new AlarmOperationData[size];
        }
    };

    private AlarmOperationData(Parcel in) {
        time = Calendar.getInstance();
        for (int field : TIME_FIELDS) {
            time.set(field, in.readInt());
        }
        message = in.readString();
        absolute = in.readByte() != 0;
    }
}
