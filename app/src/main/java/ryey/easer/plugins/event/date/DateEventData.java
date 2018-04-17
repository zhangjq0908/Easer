/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.event.date;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Locale;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

public class DateEventData extends TypedEventData {
    private static final SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private static String DateToText(Calendar calendar) {
        return sdf_date.format(calendar.getTime());
    }

    private static Calendar TextToDate(String text) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf_date.parse(text));
        return calendar;
    }

    Calendar date = null;

    {
        default_type = EventType.after;
        availableTypes = EnumSet.of(EventType.after, EventType.before, EventType.is);
    }

    public DateEventData() {}

    public DateEventData(Calendar date) {
        this.date = date;
    }

    DateEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (date == null)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DateEventData))
            return false;
        if (!Utils.eEquals(this, (EventData) obj))
            return false;
        if (!DateToText(date).equals(DateToText(((DateEventData) obj).date)))
            return false;
        return true;
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    this.date = TextToDate(data);
                } catch (ParseException e) {
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
                res = DateToText(date);
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTimeInMillis());
    }

    public static final Parcelable.Creator<DateEventData> CREATOR
            = new Parcelable.Creator<DateEventData>() {
        public DateEventData createFromParcel(Parcel in) {
            return new DateEventData(in);
        }

        public DateEventData[] newArray(int size) {
            return new DateEventData[size];
        }
    };

    private DateEventData(Parcel in) {
        date = Calendar.getInstance();
        date.setTimeInMillis(in.readLong());
    }

}
