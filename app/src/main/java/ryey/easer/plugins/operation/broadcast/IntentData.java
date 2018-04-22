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

package ryey.easer.plugins.operation.broadcast;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.Utils;

public class IntentData implements Parcelable {

    static class ExtraItem implements Parcelable {
        String key, value, type;

        public ExtraItem() {

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

        public static final Parcelable.Creator<ExtraItem> CREATOR
                = new Parcelable.Creator<ExtraItem>() {
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

    String action;
    List<String> category;
    String type;
    Uri data;
    List<ExtraItem> extras;

    IntentData() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof IntentData))
            return false;
        if (!Utils.nullableEqual(action, ((IntentData) obj).action))
            return false;
        if (!Utils.nullableEqual(category, ((IntentData) obj).category))
            return false;
        if (!Utils.nullableEqual(type, ((IntentData) obj).type))
            return false;
        if (!Utils.nullableEqual(data, ((IntentData) obj).data))
            return false;
        if (!Utils.nullableEqual(extras, ((IntentData) obj).extras))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("action:%s category:%s type:%s data:%s", action, category, type, data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeStringList(category);
        dest.writeString(type);
        dest.writeParcelable(data, 0);
        dest.writeTypedList(extras);
    }

    public static final Parcelable.Creator<IntentData> CREATOR
            = new Parcelable.Creator<IntentData>() {
        public IntentData createFromParcel(Parcel in) {
            return new IntentData(in);
        }

        public IntentData[] newArray(int size) {
            return new IntentData[size];
        }
    };

    private IntentData(Parcel in) {
        action = in.readString();
        List<String> cat = new ArrayList<>();
        in.readStringList(cat);
        if (cat.size() > 0)
            category = cat;
        type = in.readString();
        data = in.readParcelable(Uri.class.getClassLoader());
        List<ExtraItem> ext = new ArrayList<>();
        in.readTypedList(ext, ExtraItem.CREATOR);
        if (ext.size() > 0)
            extras = ext;
    }
}
