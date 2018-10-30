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
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.commons.local_plugin.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.plugins.event.AbstractEventData;

public class TimerEventData extends AbstractEventData {

    private static final String K_SHORT = "short?";
    private static final String K_SECONDS = "seconds";
    private static final String K_MINUTES = "time";
    private static final String K_EXACT_BOOL = "exact?";
    private static final String K_REPEAT_BOOL = "repeat?";

    final boolean shortTime;
    final int time;
    final Boolean exact;
    final boolean repeat;

    TimerEventData(boolean shortTime, int time, Boolean exact, boolean repeat) {
        this.shortTime = shortTime;
        this.time = time;
        this.exact = exact;
        this.repeat = repeat;
    }

    TimerEventData(int seconds, boolean repeat) {
        this(true, seconds, null, repeat);
    }

    TimerEventData(int minutes, boolean exact, boolean repeat) {
        this(false, minutes, exact, repeat);
    }

    TimerEventData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    shortTime = jsonObject.optBoolean(K_SHORT, false);
                    if (shortTime) {
                        time = jsonObject.getInt(K_SECONDS);
                        exact = null;
                    } else {
                        time = jsonObject.getInt(K_MINUTES);
                        exact = jsonObject.getBoolean(K_EXACT_BOOL);
                    }
                    repeat = jsonObject.getBoolean(K_REPEAT_BOOL);
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (time <= 0)
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
        if (obj == null || !(obj instanceof TimerEventData))
            return false;
        if (shortTime != ((TimerEventData) obj).shortTime)
            return false;
        if (time != ((TimerEventData) obj).time)
            return false;
        if (repeat != ((TimerEventData) obj).repeat)
            return false;
        if (exact != ((TimerEventData) obj).exact)
            return false;
        return true;
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(K_SHORT, shortTime);
                    if (shortTime)
                        jsonObject.put(K_SECONDS, time);
                    else {
                        jsonObject.put(K_MINUTES, time);
                        jsonObject.put(K_EXACT_BOOL, exact);
                    }
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
        dest.writeByte((byte) (shortTime ? 1 : 0));
        dest.writeInt(time);
        if (!shortTime) {
            dest.writeByte((byte) (exact ? 1 : 0));
        }
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
        shortTime = in.readByte() != 0;
        time = in.readInt();
        if (shortTime) {
            exact = null;
        } else {
            exact = in.readByte() != 0;
        }
        repeat = in.readByte() != 0;
    }
}
