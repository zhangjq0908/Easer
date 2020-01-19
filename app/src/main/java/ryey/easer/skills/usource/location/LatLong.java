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

package ryey.easer.skills.usource.location;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;

final class LatLong implements Parcelable {

    static LatLong fromString(String repr) throws ParseException {
        String[] parts = repr.split(", ");
        NumberFormat nf = NumberFormat.getNumberInstance();
        return new LatLong(
                Objects.requireNonNull(nf.parse(parts[0])).doubleValue(),
                Objects.requireNonNull(nf.parse(parts[1])).doubleValue());
    }

    final double latitude;
    final double longitude;

    LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected LatLong(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%f, %f", latitude, longitude);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LatLong> CREATOR = new Creator<LatLong>() {
        @Override
        public LatLong createFromParcel(Parcel in) {
            return new LatLong(in);
        }

        @Override
        public LatLong[] newArray(int size) {
            return new LatLong[size];
        }
    };

    @Override
    public int hashCode() {
        return Double.valueOf(latitude).hashCode() + Double.valueOf(longitude).hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof LatLong))
            return false;
        return latitude == ((LatLong) obj).latitude
                && longitude == ((LatLong) obj).longitude;
    }
}
