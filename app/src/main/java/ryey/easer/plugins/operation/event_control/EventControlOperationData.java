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

package ryey.easer.plugins.operation.event_control;

import android.os.Parcel;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

public class EventControlOperationData implements OperationData {
    public static final String K_EVENTNAME = "event name";
    public static final String K_NEWSTATUS = "new status";

    String eventName;
    boolean newStatus;

    public EventControlOperationData() {
    }

    public EventControlOperationData(String eventName, boolean newStatus) {
        this.eventName = eventName;
        this.newStatus = newStatus;
    }

    EventControlOperationData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @Override
    public void parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    eventName = jsonObject.getString(K_EVENTNAME);
                    newStatus = jsonObject.getBoolean(K_NEWSTATUS);
                } catch (JSONException e) {
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
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(K_EVENTNAME, eventName);
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
        if (!(obj instanceof EventControlOperationData))
            return false;
        if (!eventName.equals(((EventControlOperationData) obj).eventName))
            return false;
        if (newStatus != ((EventControlOperationData) obj).newStatus)
            return false;
        return true;
    }

    @Override
    public boolean isValid() {
        if (Utils.isBlank(eventName))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eventName);
        parcel.writeByte((byte) (newStatus ? 1 : 0));
    }

    public static final Creator<EventControlOperationData> CREATOR
            = new Creator<EventControlOperationData>() {
        public EventControlOperationData createFromParcel(Parcel in) {
            return new EventControlOperationData(in);
        }

        public EventControlOperationData[] newArray(int size) {
            return new EventControlOperationData[size];
        }
    };

    private EventControlOperationData(Parcel in) {
        eventName = in.readString();
        newStatus = in.readByte() != 0;
    }
}
