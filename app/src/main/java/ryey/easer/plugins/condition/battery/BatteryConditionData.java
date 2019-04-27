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

package ryey.easer.plugins.condition.battery;

import android.os.Parcel;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionData;
import ryey.easer.plugin.PluginDataFormat;

public class BatteryConditionData implements ConditionData {

    Integer battery_status = null;

    public BatteryConditionData(Integer battery_status) {
        this.battery_status = battery_status;
    }

    BatteryConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                battery_status = Integer.parseInt(data);
                break;
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                res = String.valueOf(battery_status);
                break;
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if ((battery_status == BatteryStatus.charging) || (battery_status == BatteryStatus.discharging))
            return true;
        return false;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BatteryConditionData))
            return false;
        if (!battery_status.equals(((BatteryConditionData) obj).battery_status))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(battery_status);
    }

    public static final Creator<BatteryConditionData> CREATOR
            = new Creator<BatteryConditionData>() {
        public BatteryConditionData createFromParcel(Parcel in) {
            return new BatteryConditionData(in);
        }

        public BatteryConditionData[] newArray(int size) {
            return new BatteryConditionData[size];
        }
    };

    private BatteryConditionData(Parcel in) {
        battery_status = in.readInt();
    }
}
