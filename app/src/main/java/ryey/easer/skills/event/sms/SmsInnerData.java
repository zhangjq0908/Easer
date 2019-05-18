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

package ryey.easer.skills.event.sms;

import android.os.Parcel;
import android.os.Parcelable;

import ryey.easer.Utils;

class SmsInnerData implements Parcelable {
    String sender;
    String content;

    SmsInnerData() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof SmsInnerData))
            return false;
        if (!Utils.nullableEqual(sender, ((SmsInnerData) obj).sender))
            return false;
        if (!Utils.nullableEqual(content, ((SmsInnerData) obj).content))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sender);
        dest.writeString(content);
    }

    public static final Parcelable.Creator<SmsInnerData> CREATOR
            = new Parcelable.Creator<SmsInnerData>() {
        public SmsInnerData createFromParcel(Parcel in) {
            return new SmsInnerData(in);
        }

        public SmsInnerData[] newArray(int size) {
            return new SmsInnerData[size];
        }
    };

    private SmsInnerData(Parcel in) {
        sender = in.readString();
        content = in.readString();
    }
}
