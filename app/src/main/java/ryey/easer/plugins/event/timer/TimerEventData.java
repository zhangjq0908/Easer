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

package ryey.easer.plugins.event.timer;

import android.os.Parcel;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.plugins.event.AbstractEventData;

public class TimerEventData extends AbstractEventData {

    private static final String K_MINUTES = "minutes";
    private static final String K_EXACT_BOOL = "exact?";
    private static final String K_REPEAT_BOOL = "repeat?";

    final int minutes;
    final boolean exact;
    final boolean repeat;

    TimerEventData(int minutes, boolean exact, boolean repeat) {
        this.minutes = minutes;
        this.exact = exact;
        this.repeat = repeat;
    }

    TimerEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    minutes = jsonObject.getInt(K_MINUTES);
                    exact = jsonObject.getBoolean(K_EXACT_BOOL);
                    repeat = jsonObject.getBoolean(K_REPEAT_BOOL);
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (minutes <= 0)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TimerEventData))
            return false;
        if (minutes != ((TimerEventData) obj).minutes)
            return false;
        if (repeat != ((TimerEventData) obj).repeat)
            return false;
        if (exact != ((TimerEventData) obj).exact)
            return false;
        return true;
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(K_MINUTES, minutes);
                    jsonObject.put(K_EXACT_BOOL, exact);
                    jsonObject.put(K_REPEAT_BOOL, repeat);
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStateException(e);
                }
                res = jsonObject.toString();
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(minutes);
        dest.writeByte((byte) (exact ? 1 : 0));
        dest.writeByte((byte) (repeat ? 1 : 0));
    }

    public static final Creator<TimerEventData> CREATOR
            = new Creator<TimerEventData>() {
        public TimerEventData createFromParcel(Parcel in) {
            return new TimerEventData(in);
        }

        public TimerEventData[] newArray(int size) {
            return new TimerEventData[size];
        }
    };

    private TimerEventData(Parcel in) {
        minutes = in.readInt();
        exact = in.readByte() != 0;
        repeat = in.readByte() != 0;
    }
}
