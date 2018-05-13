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

package ryey.easer.plugins.event.condition_event;

import android.os.Parcel;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.plugins.event.AbstractEventData;

public class ConditionEventEventData extends AbstractEventData {

    enum ConditionEvent {
        enter,
        leave,
    }

    private static final String K_CONDITION_NAME = "condition_name";
    private static final String K_CONDITION_EVENT = "condition_event";

    final String conditionName;
    final ConditionEvent conditionEvent;

    ConditionEventEventData(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    conditionName = jsonObject.getString(K_CONDITION_NAME);
                    conditionEvent = ConditionEvent.valueOf(jsonObject.getString(K_CONDITION_EVENT));
                } catch (JSONException e) {
                    throw new IllegalStorageDataException(e);
                }
        }
    }

    ConditionEventEventData(String conditionName, ConditionEvent conditionEvent) {
        this.conditionName = conditionName;
        this.conditionEvent = conditionEvent;
    }

    @NonNull
    @Override
    public String serialize(@NonNull C.Format format) {
        String res;
        switch (format) {
            default:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(K_CONDITION_NAME, conditionName);
                    jsonObject.put(K_CONDITION_EVENT, conditionEvent.name());
                    res = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IllegalStateException(e);
                }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ConditionEventEventData))
            return false;
        return conditionEvent.equals(((ConditionEventEventData) obj).conditionEvent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(conditionName);
        dest.writeInt(conditionEvent.ordinal());
    }

    public static final Creator<ConditionEventEventData> CREATOR
            = new Creator<ConditionEventEventData>() {
        public ConditionEventEventData createFromParcel(Parcel in) {
            return new ConditionEventEventData(in);
        }

        public ConditionEventEventData[] newArray(int size) {
            return new ConditionEventEventData[size];
        }
    };

    private ConditionEventEventData(Parcel in) {
        conditionName = in.readString();
        conditionEvent = ConditionEvent.values()[in.readInt()];
    }
}
