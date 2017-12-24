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

package ryey.easer.plugins.operation.brightness;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.plugins.operation.IntegerOperationData;

public class BrightnessOperationData extends IntegerOperationData {

    {
        lbound = -1;
        rbound = 255;
    }

    public BrightnessOperationData() {
    }

    public BrightnessOperationData(boolean auto) {
        super(-1);
    }

    public BrightnessOperationData(Integer level) {
        super(level);
    }

    BrightnessOperationData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        super(data, format, version);
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    boolean isAuto() {
        return get().equals(-1);
    }

    public static final Parcelable.Creator<BrightnessOperationData> CREATOR
            = new Parcelable.Creator<BrightnessOperationData>() {
        public BrightnessOperationData createFromParcel(Parcel in) {
            return new BrightnessOperationData(in);
        }

        public BrightnessOperationData[] newArray(int size) {
            return new BrightnessOperationData[size];
        }
    };

    private BrightnessOperationData(Parcel in) {
        super(in);
    }
}
