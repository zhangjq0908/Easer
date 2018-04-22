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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.EnumSet;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

public class TimerEventData extends TypedEventData {

    private static final String K_MINUTES = "minutes";
    private static final String K_EXACT_BOOL = "exact?";
    private static final String K_REPEAT_BOOL = "repeat?";

    Timer timer;

    {
        default_type = EventType.after;
        availableTypes = EnumSet.of(EventType.after);
    }

    public TimerEventData() {}

    public TimerEventData(Timer timer) {
        this.timer = timer;
    }

    TimerEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (timer == null)
            return false;
        if (timer.minutes <= 0)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TimerEventData))
            return false;
        if (!Utils.eEquals(this, (EventData) obj))
            return false;
        if (timer.minutes != ((TimerEventData) obj).timer.minutes)
            return false;
        if (timer.repeat != ((TimerEventData) obj).timer.repeat)
            return false;
        if (timer.exact != ((TimerEventData) obj).timer.exact)
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        throw new IllegalAccessError();
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        throw new IllegalAccessError();
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        timer = new Timer();
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    timer.minutes = jsonObject.getInt(K_MINUTES);
                    timer.exact = jsonObject.getBoolean(K_EXACT_BOOL);
                    timer.repeat = jsonObject.getBoolean(K_REPEAT_BOOL);
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
                    jsonObject.put(K_MINUTES, timer.minutes);
                    jsonObject.put(K_EXACT_BOOL, timer.exact);
                    jsonObject.put(K_REPEAT_BOOL, timer.repeat);
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
        dest.writeInt(timer.minutes);
        dest.writeByte((byte) (timer.exact ? 1 : 0));
        dest.writeByte((byte) (timer.repeat ? 1 : 0));
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
        timer = new Timer();
        timer.minutes = in.readInt();
        timer.exact = in.readByte() != 0;
        timer.repeat = in.readByte() != 0;
    }

    static class Timer {
        int minutes;
        boolean exact;
        boolean repeat;
    }
}
