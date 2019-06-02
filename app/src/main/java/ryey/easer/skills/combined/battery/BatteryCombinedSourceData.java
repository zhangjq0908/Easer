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

package ryey.easer.skills.combined.battery;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.combined_source.CombinedSourceData;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;

public class BatteryCombinedSourceData implements CombinedSourceData {

    Integer battery_status = null;

    public BatteryCombinedSourceData(Integer battery_status) {
        this.battery_status = battery_status;
    }

    BatteryCombinedSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
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

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BatteryCombinedSourceData))
            return false;
        if (!battery_status.equals(((BatteryCombinedSourceData) obj).battery_status))
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

    public static final Creator<BatteryCombinedSourceData> CREATOR
            = new Creator<BatteryCombinedSourceData>() {
        public BatteryCombinedSourceData createFromParcel(Parcel in) {
            return new BatteryCombinedSourceData(in);
        }

        public BatteryCombinedSourceData[] newArray(int size) {
            return new BatteryCombinedSourceData[size];
        }
    };

    private BatteryCombinedSourceData(Parcel in) {
        battery_status = in.readInt();
    }
}
