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

package ryey.easer.plugins.event.sms;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.plugins.event.AbstractEventData;

public class SmsEventData extends AbstractEventData {

    private static final String K_SENDER = "sender";
    private static final String K_CONTENT = "content";

    SmsInnerData innerData;

    public SmsEventData(SmsInnerData innerData) {
        this.innerData = innerData;
    }

    SmsEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (innerData == null)
            return false;
        return true;
    }

    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        innerData = new SmsInnerData();
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    innerData.sender = jsonObject.optString(K_SENDER, null);
                    innerData.content = jsonObject.optString(K_CONTENT, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String res;
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (!Utils.isBlank(innerData.sender))
                        jsonObject.put(K_SENDER, innerData.sender);
                    if (!Utils.isBlank(innerData.content))
                        jsonObject.put(K_CONTENT, innerData.content);
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    Logger.e(e, "Error serializing %s", getClass().getSimpleName());
                    throw new IllegalStateException(e.getMessage());
                }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof SmsEventData))
            return false;
        return innerData.equals(((SmsEventData) obj).innerData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(innerData, flags);
    }

    public static final Parcelable.Creator<SmsEventData> CREATOR
            = new Parcelable.Creator<SmsEventData>() {
        public SmsEventData createFromParcel(Parcel in) {
            return new SmsEventData(in);
        }

        public SmsEventData[] newArray(int size) {
            return new SmsEventData[size];
        }
    };

    private SmsEventData(Parcel in) {
        innerData = in.readParcelable(SmsInnerData.class.getClassLoader());
    }
}
