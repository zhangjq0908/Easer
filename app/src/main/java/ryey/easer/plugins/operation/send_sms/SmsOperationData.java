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

package ryey.easer.plugins.operation.send_sms;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.commons.local_plugin.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_plugin.operationplugin.OperationData;
import ryey.easer.plugin.PluginDataFormat;

public class SmsOperationData implements OperationData {
    private static final String K_DEST = "destination";
    private static final String K_CONTENT = "content";

    String destination;
    String content;

    SmsOperationData(String destination, String content) {
        this.destination = destination;
        this.content = content;
    }

    SmsOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            destination = jsonObject.getString(K_DEST);
            content = jsonObject.getString(K_CONTENT);
        } catch (JSONException e) {
            Logger.e(e, "error");
            throw new IllegalStateException(e);
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(K_DEST, destination);
                    jsonObject.put(K_CONTENT, content);
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    Logger.e(e, "error");
                    throw new IllegalStateException(e);
                }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (Utils.isBlank(destination))
            return false;
        if (!PhoneNumberUtils.isWellFormedSmsAddress(destination))
            return false;
        if (Utils.isBlank(content))
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof SmsOperationData))
            return false;
        if (!destination.equals(((SmsOperationData) obj).destination))
            return false;
        if (!Utils.nullableEqual(content, ((SmsOperationData) obj).content))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(destination);
        dest.writeString(content);
    }

    public static final Parcelable.Creator<SmsOperationData> CREATOR
            = new Parcelable.Creator<SmsOperationData>() {
        public SmsOperationData createFromParcel(Parcel in) {
            return new SmsOperationData(in);
        }

        public SmsOperationData[] newArray(int size) {
            return new SmsOperationData[size];
        }
    };

    private SmsOperationData(Parcel in) {
        destination = in.readString();
        content = in.readString();
    }

    @Nullable
    @Override
    public Set<String> placeholders() {
        return Utils.extractPlaceholder(content);
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        String new_content = Utils.applyDynamics(content, dynamicsAssignment);
        return new SmsOperationData(destination, new_content);
    }
}
