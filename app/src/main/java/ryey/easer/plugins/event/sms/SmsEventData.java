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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.EnumSet;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

public class SmsEventData extends TypedEventData {

    private static final String K_SENDER = "sender";
    private static final String K_CONTENT = "content";

    SmsInnerData innerData;

    {
        default_type = EventType.is;
        availableTypes = EnumSet.of(EventType.is, EventType.after);
    }

    public SmsEventData() {}

    public SmsEventData(SmsInnerData innerData) {
        set(innerData);
    }

    SmsEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @NonNull
    @Override
    public Object get() {
        return innerData;
    }

    @Override
    public void set(@NonNull Object obj) {
        if (obj instanceof SmsInnerData) {
            innerData = (SmsInnerData) obj;
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public boolean isValid() {
        if (innerData == null)
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalStorageDataException {
        throw new IllegalAccessError();
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        throw new IllegalAccessError();
    }

    @Override
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
                    throw new IllegalStorageDataException(e.getMessage());
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
