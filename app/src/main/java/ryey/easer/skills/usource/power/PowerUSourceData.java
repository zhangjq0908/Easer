/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.skills.usource.power;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;

import ryey.easer.commons.C;
import ryey.easer.commons.CommonSkillUtils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

public class PowerUSourceData implements USourceData {

    private static final String K_STATUS = "status";
    private static final String K_CHARGING_THROUGH = "charge_through";


//    Integer battery_status = null;
    @Nonnull final BatteryStatus batteryStatus;
    @Nonnull final Set<ChargingMethod> chargingMethods = new ArraySet<>();

    public PowerUSourceData(@NonNull BatteryStatus batteryStatus, @Nullable Collection<ChargingMethod> chargingMethods) {
        this.batteryStatus = batteryStatus;
        if (batteryStatus == BatteryStatus.charging) {
            if (chargingMethods == null || chargingMethods.size() == 0)
                throw new IllegalArgumentException("Charging must have a method");
            this.chargingMethods.addAll(chargingMethods);
        }
    }

    PowerUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                if (version < C.VERSION_RENAME_BATTERY_USOURCE) {
                    try {
                        int battery_status = Integer.parseInt(data);
                        switch (battery_status) {
                            case BatteryStatus_Old.charging:
                                batteryStatus = BatteryStatus.charging;
                                chargingMethods.add(ChargingMethod.any);
                                break;
                            case BatteryStatus_Old.discharging:
                                batteryStatus = BatteryStatus.discharging;
                                break;
                            default:
                                throw new IllegalStorageDataException("unknown battery status representation");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalStorageDataException(e);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        batteryStatus = BatteryStatus.valueOf(jsonObject.getString(K_STATUS));
                        if (batteryStatus == BatteryStatus.charging) {
                            JSONArray jsonArray = jsonObject.getJSONArray(K_CHARGING_THROUGH);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                chargingMethods.add(ChargingMethod.valueOf(jsonArray.getString(0)));
                            }
                        }
                    } catch (JSONException e) {
                        throw new IllegalStorageDataException(e);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalStorageDataException(e);
                    }
                }
                break;
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
                    jsonObject.put(K_STATUS, batteryStatus.toString());
                    if (batteryStatus == BatteryStatus.charging) {
                        JSONArray jsonArray = new JSONArray();
                        for (ChargingMethod method : chargingMethods) {
                            jsonArray.put(method.toString());
                        }
                        jsonObject.put(K_CHARGING_THROUGH, jsonArray);
                    }
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
                break;
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (batteryStatus == BatteryStatus.discharging && chargingMethods.size() > 0)
            return false;
        if (batteryStatus == BatteryStatus.charging && chargingMethods.size() == 0)
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
        if (!(obj instanceof PowerUSourceData))
            return false;
        if (!batteryStatus.equals(((PowerUSourceData) obj).batteryStatus))
            return false;
        if (!chargingMethods.equals(((PowerUSourceData) obj).chargingMethods))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(batteryStatus.ordinal());
        CommonSkillUtils.IO.writeEnumCollectionToParcel(dest, flags, chargingMethods);
    }

    public static final Creator<PowerUSourceData> CREATOR
            = new Creator<PowerUSourceData>() {
        public PowerUSourceData createFromParcel(Parcel in) {
            return new PowerUSourceData(in);
        }

        public PowerUSourceData[] newArray(int size) {
            return new PowerUSourceData[size];
        }
    };

    private PowerUSourceData(Parcel in) {
        batteryStatus = BatteryStatus.values()[in.readInt()];
        chargingMethods.addAll(CommonSkillUtils.IO.readEnumCollectionFromParcel(in, ChargingMethod.values()));
    }
}
