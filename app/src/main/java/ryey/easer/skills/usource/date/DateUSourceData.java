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

package ryey.easer.skills.usource.date;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ryey.easer.commons.C;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

public class DateUSourceData implements USourceData {
    private static final SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private static String DateToText(Calendar calendar) {
        return sdf_date.format(calendar.getTime());
    }

    private static Calendar TextToDate(String text) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf_date.parse(text));
        return calendar;
    }

    enum Rel {
        after,
    }

    private static final String K_DATE = "date";
    private static final String K_REL = "rel";

    Calendar date;
    Rel rel;

    public DateUSourceData(Calendar date, Rel rel) {
        this.date = date;
        this.rel = rel;
    }

    DateUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    try {
                        this.date = TextToDate(jsonObject.getString(K_DATE));
                    } catch (ParseException e) {
                        throw new IllegalStorageDataException(e);
                    }
                    this.rel = Rel.valueOf(jsonObject.getString(K_REL));
                } catch (JSONException e) {
                    if (version < C.VERSION_UNIFORMED_SOURCE) {
                        try {
                            this.date = TextToDate(data);
                        } catch (ParseException e1) {
                            throw new IllegalStorageDataException(e1);
                        }
                        this.rel = Rel.after;
                    } else {
                        throw new IllegalStorageDataException(e);
                    }
                }
        }

    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (date == null)
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
        if (obj == null || !(obj instanceof DateUSourceData))
            return false;
        if (!rel.equals(((DateUSourceData) obj).rel))
            return false;
        if (!DateToText(date).equals(DateToText(((DateUSourceData) obj).date)))
            return false;
        return true;
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(K_DATE, DateToText(date));
                    jsonObject.put(K_REL, rel.name());
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStateException("Failed to serialize DateConditionData");
                }
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
        dest.writeInt(rel.ordinal());
    }

    public static final Parcelable.Creator<DateUSourceData> CREATOR
            = new Parcelable.Creator<DateUSourceData>() {
        public DateUSourceData createFromParcel(Parcel in) {
            return new DateUSourceData(in);
        }

        public DateUSourceData[] newArray(int size) {
            return new DateUSourceData[size];
        }
    };

    private DateUSourceData(Parcel in) {
        date = Calendar.getInstance();
        date.setTimeInMillis(in.readLong());
        rel = Rel.values()[in.readInt()];
    }

}
