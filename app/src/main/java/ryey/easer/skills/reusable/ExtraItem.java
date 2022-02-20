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

package ryey.easer.skills.reusable;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ExtraItem implements Parcelable {
    @NonNull
    public final String key;
    @NonNull public final String value;
    @NonNull public final String type;

    public ExtraItem(@NonNull String key, @NonNull String value, @NonNull String type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof ExtraItem))
            return false;
        if (!key.equals(((ExtraItem) obj).key))
            return false;
        if (!value.equals(((ExtraItem) obj).value))
            return false;
        if (!type.equals(((ExtraItem) obj).type))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
        dest.writeString(type);
    }

    public static final Creator<ExtraItem> CREATOR
            = new Creator<ExtraItem>() {
        public ExtraItem createFromParcel(Parcel in) {
            return new ExtraItem(in);
        }

        public ExtraItem[] newArray(int size) {
            return new ExtraItem[size];
        }
    };

    private ExtraItem(Parcel in) {
        key = in.readString();
        value = in.readString();
        type = in.readString();
    }
}
