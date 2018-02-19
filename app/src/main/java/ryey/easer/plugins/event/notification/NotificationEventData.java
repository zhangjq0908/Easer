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

package ryey.easer.plugins.event.notification;

import android.os.Parcel;
import android.support.annotation.NonNull;

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
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

public class NotificationEventData extends TypedEventData {
    private static final String K_APP = "app";
    private static final String K_TITLE = "title";
    private static final String K_CONTENT = "content";

    String app;
    String title;
    String content;

    {
        default_type = EventType.after;
        availableTypes = EnumSet.of(EventType.after, EventType.is);
    }

    public NotificationEventData() {}

    NotificationEventData(String app, String title, String content) {
        this.app = app;
        this.title = title;
        this.content = content;
    }

    NotificationEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (app != null)
            return true;
        if (title != null)
            return true;
        if (content != null)
            return true;
        return false;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof NotificationEventData))
            return false;
        if (!((NotificationEventData) obj).isValid())
            return false;
        if (!Utils.eEquals(this, (EventData) obj))
            return false;
        if (!Utils.nullableEqual(app, ((NotificationEventData) obj).app))
            return false;
        if (!Utils.nullableEqual(title, ((NotificationEventData) obj).title))
            return false;
        if (!Utils.nullableEqual(content, ((NotificationEventData) obj).content))
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
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    app = jsonObject.optString(K_APP, null);
                    title = jsonObject.optString(K_TITLE, null);
                    content = jsonObject.optString(K_CONTENT, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String res;
        switch (format) {
            default:
                JSONObject jsonObject = new JSONObject();
                try {
                    if (!Utils.isBlank(app))
                        jsonObject.put(K_APP, app);
                    if (!Utils.isBlank(title))
                        jsonObject.put(K_TITLE, title);
                    if (!Utils.isBlank(content))
                        jsonObject.put(K_CONTENT, content);
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
                res = jsonObject.toString();
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(app);
        dest.writeString(title);
        dest.writeString(content);
    }

    public static final Creator<NotificationEventData> CREATOR
            = new Creator<NotificationEventData>() {
        public NotificationEventData createFromParcel(Parcel in) {
            return new NotificationEventData(in);
        }

        public NotificationEventData[] newArray(int size) {
            return new NotificationEventData[size];
        }
    };

    private NotificationEventData(Parcel in) {
        app = in.readString();
        title = in.readString();
        content = in.readString();
    }
}
