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

package ryey.easer.skills.operation.toast;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.operation.DynamicsEnabledString;

public class ToastOperationData implements OperationData {

    private static final String K_TEXT = "text";

    @NonNull final DynamicsEnabledString text;

    ToastOperationData(String text) {
        this(new DynamicsEnabledString(text));
    }

    ToastOperationData(DynamicsEnabledString text) {
        this.text = text;
    }

    ToastOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    text = new DynamicsEnabledString(jsonObject.getString(K_TEXT));
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String s = "";
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(K_TEXT, text.str);
                    s = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        return s;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof ToastOperationData))
            return false;
        return text.equals(((ToastOperationData) obj).text);
    }

    @Nullable
    @Override
    public Set<String> placeholders() {
        return text.placeholders();
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        return new ToastOperationData(text.applyDynamics(dynamicsAssignment));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text.str);
    }

    public static final Creator<ToastOperationData> CREATOR
            = new Creator<ToastOperationData>() {
        public ToastOperationData createFromParcel(Parcel in) {
            return new ToastOperationData(in);
        }

        public ToastOperationData[] newArray(int size) {
            return new ToastOperationData[size];
        }
    };

    private ToastOperationData(Parcel in) {
        text = new DynamicsEnabledString(in.readString());
    }
}
