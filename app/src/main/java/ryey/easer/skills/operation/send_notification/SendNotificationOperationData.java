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

package ryey.easer.skills.operation.send_notification;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.plugin.PluginDataFormat;

public class SendNotificationOperationData implements OperationData {
    private static final String K_TITLE = "title";
    private static final String K_CONTENT = "content";

    String title;
    String content;

    SendNotificationOperationData(String title, String content) {
        this.title = title;
        this.content = content;
    }

    SendNotificationOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    title = jsonObject.getString(K_TITLE);
                    content = jsonObject.getString(K_CONTENT);
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
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
                    jsonObject.put(K_TITLE, title);
                    jsonObject.put(K_CONTENT, content);
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
                res = jsonObject.toString();
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (title == null)
            return false;
        if (content == null)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof SendNotificationOperationData))
            return false;
        if (!((SendNotificationOperationData) obj).isValid())
            return false;
        if (!Utils.nullableEqual(title,
                ((SendNotificationOperationData) obj).title))
            return false;
        if (!Utils.nullableEqual(content,
                ((SendNotificationOperationData) obj).content))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(content);
    }

    public static final Creator<SendNotificationOperationData> CREATOR
            = new Creator<SendNotificationOperationData>() {
        public SendNotificationOperationData createFromParcel(Parcel in) {
            return new SendNotificationOperationData(in);
        }

        public SendNotificationOperationData[] newArray(int size) {
            return new SendNotificationOperationData[size];
        }
    };

    private SendNotificationOperationData(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    @Nullable
    @Override
    public Set<String> placeholders() {
        Set<String> placeholders = Utils.extractPlaceholder(title);
        placeholders.addAll(Utils.extractPlaceholder(content));
        return placeholders;
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        String new_title = Utils.applyDynamics(title, dynamicsAssignment);
        String new_content = Utils.applyDynamics(content, dynamicsAssignment);
        return new SendNotificationOperationData(new_title, new_content);
    }
}
