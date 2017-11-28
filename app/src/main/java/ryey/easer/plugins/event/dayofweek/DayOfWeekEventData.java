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

package ryey.easer.plugins.event.dayofweek;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.event.TypedEventData;

public class DayOfWeekEventData extends TypedEventData {

    Set<Integer> days = new ArraySet<>(7);

    {
        default_type = EventType.any;
        availableTypes = EnumSet.of(EventType.any, EventType.none);
    }

    public DayOfWeekEventData() {}

    public DayOfWeekEventData(Set<Integer> days) {
        this.days = days;
    }

    @NonNull
    @Override
    public Object get() {
        return days;
    }

    @Override
    public void set(@NonNull Object obj) {
        if (obj instanceof String) {
            set(((String) obj).split("\n"));
        } else if (obj instanceof String[]) {
            for (String str : (String[]) obj) {
                days.add(Integer.parseInt(str.trim()));
            }
        } else if (obj instanceof Set) {
            for (Object o : (Set) obj) {
                if (o instanceof Integer)
                    days.add((Integer) o);
                else
                    Logger.wtf("Data is Set but element <%s> is not Integer", o);
            }
        } else {
            throw new RuntimeException("illegal data type");
        }
    }

    @Override
    public boolean isValid() {
        if (days == null)
            return false;
        if (days.isEmpty())
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        if (version == C.VERSION_DEFAULT) {
            String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
            try {
                set(Utils.str2set(str_data));
            } catch (ParseException e) {
                Logger.e(e, "Illegal Event: illegal time format %s", str_data);
                throw new IllegalStorageDataException(String.format("Illegal Event: illegal time format %s", str_data));
            }
        } else {
            set(XmlHelper.EventHelper.readMultipleSituation(parser));
        }
        EventType type = XmlHelper.EventHelper.readLogic(parser);
        setType(type);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        if (!isValid()) {
            Logger.wtf("Invalid DayOfWeekEventData shouldn't be serialized");
        }
        XmlHelper.EventHelper.writeMultipleSituation(serializer, PluginRegistry.getInstance().event().findPlugin(this).name(),
                Utils.set2strlist(days).toArray(new String[0]));
        XmlHelper.EventHelper.writeLogic(serializer, type());
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        days.clear();
        switch (format) {
            default:
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        days.add(jsonArray.getInt(i));
                    }
                } catch (JSONException e) {
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
                JSONArray jsonArray = new JSONArray();
                for (Integer v : days) {
                    jsonArray.put(v);
                }
                res = jsonArray.toString();
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(new ArrayList<>(days));
    }

    public static final Parcelable.Creator<DayOfWeekEventData> CREATOR
            = new Parcelable.Creator<DayOfWeekEventData>() {
        public DayOfWeekEventData createFromParcel(Parcel in) {
            return new DayOfWeekEventData(in);
        }

        public DayOfWeekEventData[] newArray(int size) {
            return new DayOfWeekEventData[size];
        }
    };

    private DayOfWeekEventData(Parcel in) {
        ArrayList<Integer> list = new ArrayList<>();
        in.readList(list, null);
        days = new ArraySet<>(list);
    }
}
