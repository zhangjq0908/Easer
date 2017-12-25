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

package ryey.easer.plugins.event.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

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
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.event.TypedEventData;

public class WifiEventData extends TypedEventData {
    List<String> ssids = new ArrayList<>();

    {
        default_type = EventType.any;
        availableTypes = EnumSet.of(EventType.any, EventType.none);
    }

    public WifiEventData() {}

    public WifiEventData(String ssid) {
        setFromMultiple(ssid.split("\n"));
    }

    WifiEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    private void setFromMultiple(String[] ssids) {
        this.ssids = new ArrayList<>();
        for (String hardware_address : ssids) {
            if (!Utils.isBlank(hardware_address))
                this.ssids.add(hardware_address.trim());
        }
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        boolean is_first = true;
        for (String ssid : ssids) {
            if (!is_first)
                text.append("\n");
            text.append(ssid);
            is_first = false;
        }
        return text.toString();
    }

    @Override
    public boolean isValid() {
        if (ssids.size() == 0)
            return false;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof WifiEventData))
            return false;
        if (!Utils.eEquals(this, (EventData) obj))
            return false;
        if (!ssids.equals(((WifiEventData) obj).ssids))
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        if (version == C.VERSION_DEFAULT) {
            String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
            setFromMultiple(str_data.split("\n"));
        } else {
            setFromMultiple(XmlHelper.EventHelper.readMultipleSituation(parser));
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
            Logger.wtf("Invalid SmsEventData shouldn't be serialized");
        }
        XmlHelper.EventHelper.writeMultipleSituation(serializer, PluginRegistry.getInstance().event().findPlugin(this).id(), ssids.toArray(new String[0]));
        XmlHelper.EventHelper.writeLogic(serializer, type());
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        ssids.clear();
        switch (format) {
            default:
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ssids.add(jsonArray.getString(i));
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
                for (String ssid : ssids) {
                    jsonArray.put(ssid);
                }
                res = jsonArray.toString();
        }
        return res;
    }

    @Override
    public boolean match(@NonNull Object obj) {
        if (obj instanceof String) {
            return ssids.contains(((String) obj).trim());
        }
        return super.match(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(ssids);
    }

    public static final Parcelable.Creator<WifiEventData> CREATOR
            = new Parcelable.Creator<WifiEventData>() {
        public WifiEventData createFromParcel(Parcel in) {
            return new WifiEventData(in);
        }

        public WifiEventData[] newArray(int size) {
            return new WifiEventData[size];
        }
    };

    private WifiEventData(Parcel in) {
        in.readStringList(ssids);
    }
}
