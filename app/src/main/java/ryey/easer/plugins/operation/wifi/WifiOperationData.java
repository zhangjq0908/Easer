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

package ryey.easer.plugins.operation.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.plugins.operation.BooleanOperationData;

public class WifiOperationData extends BooleanOperationData {

    WifiOperationData(Boolean state) {
        super(state);
    }

    WifiOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        super(data, format, version);
    }

    public static final Parcelable.Creator<WifiOperationData> CREATOR
            = new Parcelable.Creator<WifiOperationData>() {
        public WifiOperationData createFromParcel(Parcel in) {
            return new WifiOperationData(in);
        }

        public WifiOperationData[] newArray(int size) {
            return new WifiOperationData[size];
        }
    };

    private WifiOperationData(Parcel in) {
        super(in);
    }

}
