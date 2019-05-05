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

package ryey.easer.plugins.operation.play_media;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.commons.local_plugin.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_plugin.operationplugin.OperationData;
import ryey.easer.plugin.PluginDataFormat;

public class PlayMediaOperationData implements OperationData {
    private static final String K_TARGET = "target";

    final @NonNull String filePath; //TODO: Change to Uri by DocumentsProvider

    PlayMediaOperationData(@NonNull String filePath) {
        this.filePath = filePath;
    }

    PlayMediaOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    filePath = jsonObject.getString(K_TARGET);
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res = "";
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(K_TARGET, filePath);
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        return !filePath.isEmpty();
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof PlayMediaOperationData))
            return false;
        if (!filePath.equals(((PlayMediaOperationData) obj).filePath))
            return false;
        return true;
    }

    @Nullable
    @Override
    public Set<String> placeholders() {
        return null;
    }

    @NonNull
    @Override
    public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
    }

    public static final Creator<PlayMediaOperationData> CREATOR
            = new Creator<PlayMediaOperationData>() {
        public PlayMediaOperationData createFromParcel(Parcel in) {
            return new PlayMediaOperationData(in);
        }

        public PlayMediaOperationData[] newArray(int size) {
            return new PlayMediaOperationData[size];
        }
    };

    private PlayMediaOperationData(Parcel in) {
        filePath = in.readString();
    }
}
