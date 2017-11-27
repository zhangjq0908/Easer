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

package ryey.easer.plugins.event.bluetooth_device;

import android.os.Parcel;
import android.os.Parcelable;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.event.TypedEventData;


public class BTDeviceEventData extends TypedEventData {
    List<String> hwaddresses = new ArrayList<>();

    {
        default_type = EventType.any;
        availableTypes = EnumSet.of(EventType.any, EventType.none);
    }

    public BTDeviceEventData() {}

    public BTDeviceEventData(String hardware_address) {
        set(hardware_address);
    }

    public BTDeviceEventData(String hardware_address, EventType type) {
        set(hardware_address);
        setType(type);
    }

    @Override
    public Object get() {
        return hwaddresses;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof String) {
            set(((String) obj).split("\n"));
        } else if (obj instanceof String[]) {
            for (String hardware_address : (String[]) obj) {
                if (!Utils.isBlank(hardware_address))
                    hwaddresses.add(hardware_address.trim());
            }
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public String toString() {
        String text = "";
        boolean is_first = true;
        for (String hwaddress : hwaddresses) {
            if (!is_first)
                text += "\n";
            text += hwaddress;
            is_first = false;
        }
        return text;
    }

    @Override
    public boolean isValid() {
        if (hwaddresses.size() == 0)
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        if (version == C.VERSION_DEFAULT) {
            String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
            set(str_data);
        } else {
            set(XmlHelper.EventHelper.readMultipleSituation(parser));
        }
        EventType type = XmlHelper.EventHelper.readLogic(parser);
        if (version == C.VERSION_DEFAULT) {
            if (type == EventType.is)
                type = EventType.any;
            else if (type == EventType.is_not)
                type = EventType.none;
        }
        setType(type);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        if (!isValid()) {
            Logger.wtf("Invalid data should not be serialized");
        }
        XmlHelper.EventHelper.writeMultipleSituation(serializer, PluginRegistry.getInstance().event().findPlugin(this).name(), hwaddresses.toArray(new String[0]));
        XmlHelper.EventHelper.writeLogic(serializer, type());
    }

    @Override
    public void parse(String data, C.Format format, int version) throws IllegalStorageDataException {
        hwaddresses.clear();
        switch (format) {
            default:
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String hwaddr = jsonArray.getString(i);
                        hwaddresses.add(hwaddr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e.getMessage());
                }
        }
    }

    @Override
    public String serialize(C.Format format) {
        String res = "";
        switch (format) {
            default:
                JSONArray jsonArray = new JSONArray();
                for (String hwaddr : hwaddresses) {
                    jsonArray.put(hwaddr);
                }
                res = jsonArray.toString();
        }
        return res;
    }

    @Override
    public boolean match(Object obj) {
        if (obj instanceof String) {
            return hwaddresses.contains(((String) obj).trim());
        }
        return super.match(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(hwaddresses);
    }

    public static final Parcelable.Creator<BTDeviceEventData> CREATOR
            = new Parcelable.Creator<BTDeviceEventData>() {
        public BTDeviceEventData createFromParcel(Parcel in) {
            return new BTDeviceEventData(in);
        }

        public BTDeviceEventData[] newArray(int size) {
            return new BTDeviceEventData[size];
        }
    };

    private BTDeviceEventData(Parcel in) {
        in.readStringList(hwaddresses);
    }
}
