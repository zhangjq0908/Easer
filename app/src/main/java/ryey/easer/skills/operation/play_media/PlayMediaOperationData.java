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

package ryey.easer.skills.operation.play_media;

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

public class PlayMediaOperationData implements OperationData {
    private static final String K_TARGET = "target";
    private static final String K_LOOP = "loop";
    private static final String K_REPEAT_TIMES = "repeat_times";

    final @NonNull String filePath; //TODO: Change to Uri by DocumentsProvider
    final boolean loop;
    final int repeat_times;

    PlayMediaOperationData(@NonNull String filePath, boolean loop, int repeat_times) {
        this.filePath = filePath;
        this.loop = loop;
        this.repeat_times = repeat_times;
    }

    PlayMediaOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    filePath = jsonObject.getString(K_TARGET);
                    loop = jsonObject.optBoolean(K_LOOP, false);
                    repeat_times = jsonObject.optInt(K_REPEAT_TIMES, 0);
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
                    jsonObject.put(K_LOOP, loop);
                    if (repeat_times > 0)
                        jsonObject.put(K_REPEAT_TIMES, repeat_times);
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
        return !filePath.isEmpty() && (!loop || repeat_times > 0);
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
        if (loop != ((PlayMediaOperationData) obj).loop)
            return false;
        if (loop && repeat_times != ((PlayMediaOperationData) obj).repeat_times)
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
        dest.writeByte(loop ? (byte) 1 : (byte) 0);
        dest.writeInt(repeat_times);
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
        loop = in.readByte() == 1;
        repeat_times = in.readInt();
    }
}
