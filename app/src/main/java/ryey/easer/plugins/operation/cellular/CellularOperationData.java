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

package ryey.easer.plugins.operation.cellular;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.plugins.operation.BooleanOperationData;

public class CellularOperationData extends BooleanOperationData {
    CellularOperationData(Boolean state) {
        super(state);
    }

    CellularOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        super(data, format, version);
    }

    public static final Parcelable.Creator<CellularOperationData> CREATOR
            = new Parcelable.Creator<CellularOperationData>() {
        public CellularOperationData createFromParcel(Parcel in) {
            return new CellularOperationData(in);
        }

        public CellularOperationData[] newArray(int size) {
            return new CellularOperationData[size];
        }
    };

    private CellularOperationData(Parcel in) {
        super(in);
    }

}
