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

package ryey.easer.skills.event.time;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.event.AbstractEventData;

public class TimeEventData extends AbstractEventData {
    private static final SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm", Locale.US);

    private static String TimeToText(Calendar calendar) {
        return sdf_time.format(calendar.getTime());
    }

    private static Calendar TextToTime(String text) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf_time.parse(text));
        return calendar;
    }

    Calendar time = null;

    public TimeEventData(Calendar time) {
        this.time = time;
    }

    TimeEventData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (time == null)
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
        if (obj == null || !(obj instanceof TimeEventData))
            return false;
        if (!TimeToText(time).equals(TimeToText(((TimeEventData) obj).time)))
            return false;
        return true;
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    time = TextToTime(data);
                } catch (ParseException e) {
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
                res = TimeToText(time);
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time.getTimeInMillis());
    }

    public static final Parcelable.Creator<TimeEventData> CREATOR
            = new Parcelable.Creator<TimeEventData>() {
        public TimeEventData createFromParcel(Parcel in) {
            return new TimeEventData(in);
        }

        public TimeEventData[] newArray(int size) {
            return new TimeEventData[size];
        }
    };

    private TimeEventData(Parcel in) {
        time = Calendar.getInstance();
        time.setTimeInMillis(in.readLong());
    }
}
