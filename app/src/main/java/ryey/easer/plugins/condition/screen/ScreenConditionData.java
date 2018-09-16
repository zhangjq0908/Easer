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

package ryey.easer.plugins.condition.screen;

import android.os.Parcel;
import android.support.annotation.NonNull;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.plugindef.conditionplugin.ConditionData;

public class ScreenConditionData implements ConditionData {

    enum ScreenEvent {
        on,
        off,
        unlocked,
    }

    final ScreenEvent screenEvent;

    ScreenConditionData(ScreenEvent screenEvent) {
        this.screenEvent = screenEvent;
    }

    ScreenConditionData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                screenEvent = ScreenEvent.valueOf(data);
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                res = screenEvent.name();
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (screenEvent == null)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof ScreenConditionData))
            return false;
        if (!screenEvent.equals(((ScreenConditionData) obj).screenEvent))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        screenEvent.ordinal();
    }

    public static final Creator<ScreenConditionData> CREATOR
            = new Creator<ScreenConditionData>() {
        public ScreenConditionData createFromParcel(Parcel in) {
            return new ScreenConditionData(in);
        }

        public ScreenConditionData[] newArray(int size) {
            return new ScreenConditionData[size];
        }
    };

    private ScreenConditionData(Parcel in) {
        screenEvent = ScreenEvent.values()[in.readInt()];
    }
}
