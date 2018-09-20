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

package ryey.easer.plugins.condition.time;

import android.os.Parcel;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionData;
import ryey.easer.plugin.PluginDataFormat;

public class TimeConditionData implements ConditionData {
    private static final SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm", Locale.US);

    private static String TimeToText(Calendar calendar) {
        return sdf_time.format(calendar.getTime());
    }

    private static Calendar TextToTime(String text) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf_time.parse(text));
        return calendar;
    }

    enum Rel {
        after,
    }

    private static final String K_TIME = "time";
    private static final String K_REL = "rel";

    final Calendar time;
    final Rel rel;

    TimeConditionData(Calendar time, Rel rel) {
        this.time = time;
        this.rel = rel;
    }

    TimeConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    time = TextToTime(jsonObject.getString(K_TIME));
                    rel = Rel.valueOf(jsonObject.getString(K_REL));
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                } catch (ParseException e) {
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
                    jsonObject.put(K_TIME, TimeToText(time));
                    jsonObject.put(K_REL, rel.name());
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
        if (time == null)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof TimeConditionData))
            return false;
        if (!TimeToText(time).equals(TimeToText(((TimeConditionData) obj).time)))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rel.ordinal());
        dest.writeLong(time.getTimeInMillis());
    }

    public static final Creator<TimeConditionData> CREATOR
            = new Creator<TimeConditionData>() {
        public TimeConditionData createFromParcel(Parcel in) {
            return new TimeConditionData(in);
        }

        public TimeConditionData[] newArray(int size) {
            return new TimeConditionData[size];
        }
    };

    private TimeConditionData(Parcel in) {
        rel = Rel.values()[in.readInt()];
        time = Calendar.getInstance();
        time.setTimeInMillis(in.readLong());
    }
}
