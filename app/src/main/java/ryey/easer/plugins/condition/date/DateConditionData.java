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

package ryey.easer.plugins.condition.date;

import android.os.Parcel;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.conditionplugin.ConditionData;

public class DateConditionData implements ConditionData {
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

    DateConditionData(Calendar date, Rel rel) {
        this.date = date;
        this.rel = rel;
    }

    DateConditionData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    this.date = TextToDate(jsonObject.getString(K_DATE));
                    this.rel = Rel.valueOf(jsonObject.getString(K_REL));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
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
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof DateConditionData))
            return false;
        if (!rel.equals(((DateConditionData) obj).rel))
            return false;
        if (!DateToText(date).equals(DateToText(((DateConditionData) obj).date)))
            return false;
        return true;
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

    public static final Creator<DateConditionData> CREATOR
            = new Creator<DateConditionData>() {
        public DateConditionData createFromParcel(Parcel in) {
            return new DateConditionData(in);
        }

        public DateConditionData[] newArray(int size) {
            return new DateConditionData[size];
        }
    };

    private DateConditionData(Parcel in) {
        date = Calendar.getInstance();
        date.setTimeInMillis(in.readLong());
        rel = Rel.values()[in.readInt()];
    }
}
