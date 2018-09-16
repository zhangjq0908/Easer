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

package ryey.easer.plugins.condition.wifi;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.plugindef.conditionplugin.ConditionData;

public class WifiConditionData implements ConditionData {
    private static final String K_ESSID = "essid";
    private static final String K_BSSID = "bssid";

    boolean mode_essid = true;
    Set<String> ssids = new ArraySet<>();

    WifiConditionData(String ssids, boolean mode_essid) {
        this.mode_essid = mode_essid;
        setFromMultiple(ssids.split("\n"));
    }

    WifiConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
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

    private void setFromMultiple(String[] ssids) {
        this.ssids.clear();
        for (String ssid : ssids) {
            if (!Utils.isBlank(ssid))
                this.ssids.add(ssid.trim());
        }
    }

    private void readFromJsonArray(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            ssids.add(jsonArray.getString(i));
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
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
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof WifiConditionData))
            return false;
        if (!ssids.equals(((WifiConditionData) obj).ssids))
            return false;
        return true;
    }
    
    boolean match(@NonNull Object obj) {
        if (obj instanceof String) {
            return ssids.contains(((String) obj).trim());
        }
        return equals(obj);
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

    public static final Creator<WifiConditionData> CREATOR
            = new Creator<WifiConditionData>() {
        public WifiConditionData createFromParcel(Parcel in) {
            return new WifiConditionData(in);
        }

        public WifiConditionData[] newArray(int size) {
            return new WifiConditionData[size];
        }
    };

    private WifiConditionData(Parcel in) {
        mode_essid = in.readByte() > 0;
        List<String> list = new ArrayList<>();
        in.readStringList(list);
        ssids.addAll(list);
    }
}
