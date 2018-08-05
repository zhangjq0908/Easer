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

package ryey.easer.plugins.operation.state_control;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.core.data.storage.ScriptDataStorage;

public class StateControlOperationData implements OperationData {
    public static final String K_EVENTNAME = "event name";
    private static final String K_SCRIPTNAME = "script name";
    private static final String K_NEWSTATUS = "new status";

    public final String scriptName;
    final boolean newStatus;

    public StateControlOperationData(StateControlOperationData ref, String scriptName) {
        this.scriptName = scriptName;
        newStatus = ref.newStatus;
    }

    StateControlOperationData(String scriptName, boolean newStatus) {
        this.scriptName = scriptName;
        this.newStatus = newStatus;
    }

    StateControlOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if (version < C.VERSION_RENAME_STATE_CONTROL) {
                        scriptName = jsonObject.getString(K_EVENTNAME);
                    } else {
                        scriptName = jsonObject.getString(K_SCRIPTNAME);
                    }
                    newStatus = jsonObject.getBoolean(K_NEWSTATUS);
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
                    jsonObject.put(K_SCRIPTNAME, scriptName);
                    jsonObject.put(K_NEWSTATUS, newStatus);
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof StateControlOperationData))
            return false;
        if (!scriptName.equals(((StateControlOperationData) obj).scriptName))
            return false;
        if (newStatus != ((StateControlOperationData) obj).newStatus)
            return false;
        return true;
    }

    @Override
    public boolean isValid() {
        if (Utils.isBlank(scriptName))
            return false;
        return true;
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean isValid(Context context) {
        if (!isValid())
            return false;
        ScriptDataStorage dataStorage = ScriptDataStorage.getInstance(context);
        if (!dataStorage.list().contains(scriptName))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(scriptName);
        parcel.writeByte((byte) (newStatus ? 1 : 0));
    }

    public static final Creator<StateControlOperationData> CREATOR
            = new Creator<StateControlOperationData>() {
        public StateControlOperationData createFromParcel(Parcel in) {
            return new StateControlOperationData(in);
        }

        public StateControlOperationData[] newArray(int size) {
            return new StateControlOperationData[size];
        }
    };

    private StateControlOperationData(Parcel in) {
        scriptName = in.readString();
        newStatus = in.readByte() != 0;
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
}
