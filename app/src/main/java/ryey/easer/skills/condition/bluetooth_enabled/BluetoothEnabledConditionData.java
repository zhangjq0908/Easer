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

package ryey.easer.skills.condition.bluetooth_enabled;

import android.os.Parcel;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.conditionskill.ConditionData;
import ryey.easer.plugin.PluginDataFormat;

public class BluetoothEnabledConditionData implements ConditionData {

    final boolean enabled;

    BluetoothEnabledConditionData(boolean enabled) {
        this.enabled = enabled;
    }

    BluetoothEnabledConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                enabled = Boolean.parseBoolean(data);
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                res = String.valueOf(enabled);
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof BluetoothEnabledConditionData))
            return false;
        if (enabled != ((BluetoothEnabledConditionData) obj).enabled)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (enabled ? 1 : 0));
    }

    public static final Creator<BluetoothEnabledConditionData> CREATOR
            = new Creator<BluetoothEnabledConditionData>() {
        public BluetoothEnabledConditionData createFromParcel(Parcel in) {
            return new BluetoothEnabledConditionData(in);
        }

        public BluetoothEnabledConditionData[] newArray(int size) {
            return new BluetoothEnabledConditionData[size];
        }
    };

    private BluetoothEnabledConditionData(Parcel in) {
        enabled = in.readByte() != 0;
    }
}
