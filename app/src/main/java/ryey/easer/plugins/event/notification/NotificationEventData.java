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

package ryey.easer.plugins.event.notification;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.dynamics.Dynamics;
import ryey.easer.plugins.event.AbstractEventData;

public class NotificationEventData extends AbstractEventData {
    private static final String K_APP = "app";
    private static final String K_TITLE = "title";
    private static final String K_CONTENT = "content";

    String app;
    String title;
    String content;

    NotificationEventData(String app, String title, String content) {
        this.app = app;
        this.title = title;
        this.content = content;
    }

    NotificationEventData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
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

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return new Dynamics[]{
                new AppDynamics(),
                new TitleDynamics(),
                new ContentDynamics(),
        };
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
        if (!Utils.nullableEqual(app, ((NotificationEventData) obj).app))
            return false;
        if (!Utils.nullableEqual(title, ((NotificationEventData) obj).title))
            return false;
        if (!Utils.nullableEqual(content, ((NotificationEventData) obj).content))
            return false;
        return true;
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
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
    public String serialize(@NonNull PluginDataFormat format) {
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

    static class TitleDynamics implements Dynamics {
        static final String id = "ryey.easer.plugins.event.notification.title";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.ev_notification_dynamics_title;
        }
    }

    static class ContentDynamics implements Dynamics {
        static final String id = "ryey.easer.plugins.event.notification.content";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.ev_notification_dynamics_content;
        }
    }

    static class AppDynamics implements Dynamics {
        static final String id = "ryey.easer.plugins.event.notification.app";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.ev_notification_dynamics_app;
        }
    }
}
