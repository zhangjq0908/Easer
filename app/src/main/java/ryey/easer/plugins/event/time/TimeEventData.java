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

package ryey.easer.plugins.event.time;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Locale;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.event.TypedEventData;

public class TimeEventData extends TypedEventData {
    private static final SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm", Locale.US);

    private static String TimeToText(Calendar calendar) {
        return sdf_time.format(calendar.getTime());
    }

    private static Calendar TextToTime(String text) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf_time.parse(text));
        return calendar;
    }

    private Calendar time = null;

    {
        default_type = EventType.after;
        availableTypes = EnumSet.of(EventType.after, EventType.is, EventType.is_not);
    }

    public TimeEventData() {}

    public TimeEventData(Calendar time) {
        this.time = time;
    }

    @NonNull
    @Override
    public Object get() {
        return time;
    }

    @Override
    public void set(@NonNull Object obj) {
        if (obj instanceof Calendar)
            time = (Calendar) obj;
        else {
            throw new RuntimeException("illegal data type");
        }
    }

    @Override
    public boolean isValid() {
        if (time == null)
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
        try {
            set(TextToTime(str_data));
            EventType type = XmlHelper.EventHelper.readLogic(parser);
            setType(type);
        } catch (ParseException e) {
            Logger.e(e, "Illegal Event: illegal time format %s", str_data);
            throw new IllegalStorageDataException(String.format("Illegal Event: illegal time format %s", str_data));
        }
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        Calendar time = (Calendar) get();
        if (time != null) {
            XmlHelper.EventHelper.writeSingleSituation(serializer, PluginRegistry.getInstance().event().findPlugin(this).name(), TimeToText(time));
            XmlHelper.EventHelper.writeLogic(serializer, type());
        }
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    set(TextToTime(data));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e.getMessage());
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
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
