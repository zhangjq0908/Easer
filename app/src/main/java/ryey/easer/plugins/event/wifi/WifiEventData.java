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

package ryey.easer.plugins.event.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.event.TypedEventData;

public class WifiEventData extends TypedEventData {
    private static final String K_ESSID = "essid";
    private static final String K_BSSID = "bssid";

    boolean mode_essid = true;
    Set<String> ssids = new ArraySet<>();

    {
        default_type = EventType.any;
        availableTypes = EnumSet.of(EventType.any, EventType.none);
    }

    WifiEventData() {}

    public WifiEventData(String ssid) {
        ssids.add(ssid);
    }

    WifiEventData(String ssids, boolean mode_essid) {
        this.mode_essid = mode_essid;
        setFromMultiple(ssids.split("\n"));
    }

    WifiEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    private void setFromMultiple(String[] ssids) {
        this.ssids.clear();
        for (String ssid : ssids) {
            if (!Utils.isBlank(ssid))
                this.ssids.add(ssid.trim());
        }
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (ssids.size() == 0)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof WifiEventData))
            return false;
        if (!Utils.eEquals(this, (EventData) obj))
            return false;
        if (mode_essid != ((WifiEventData) obj).mode_essid)
            return false;
        if (!ssids.equals(((WifiEventData) obj).ssids))
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        if (version == C.VERSION_FALLBACK) {
            String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
            setFromMultiple(str_data.split("\n"));
        } else {
            setFromMultiple(XmlHelper.EventHelper.readMultipleSituation(parser));
        }
        EventType type = XmlHelper.EventHelper.readLogic(parser);
        if (version == C.VERSION_FALLBACK) {
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
                    if (version < C.VERSION_WIFI_ADD_BSSID) {
                        JSONArray jsonArray = new JSONArray(data);
                        readFromJsonArray(jsonArray);
                    } else {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.has(K_ESSID)) {
                            mode_essid = true;
                            readFromJsonArray(jsonObject.getJSONArray(K_ESSID));
                        } else {
                            mode_essid = false;
                            readFromJsonArray(jsonObject.getJSONArray(K_BSSID));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    private void readFromJsonArray(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            ssids.add(jsonArray.getString(i));
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
                    JSONArray jsonArray = new JSONArray();
                    for (String ssid : ssids) {
                        jsonArray.put(ssid);
                    }
                    if (mode_essid)
                        jsonObject.put(K_ESSID, jsonArray);
                    else
                        jsonObject.put(K_BSSID, jsonArray);
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStateException();
                }
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
        dest.writeByte((byte) (mode_essid ? 1 : 0));
        dest.writeStringList(new ArrayList<>(ssids));
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
        mode_essid = in.readByte() > 0;
        List<String> list = new ArrayList<>();
        in.readStringList(list);
        ssids.addAll(list);
    }
}
