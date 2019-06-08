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

package ryey.easer.skills.usource.screen;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

public class ScreenUSourceData implements USourceData {

    enum ScreenEvent {
        on,
        off,
        unlocked,
    }

    final ScreenEvent screenEvent;

    ScreenUSourceData(ScreenEvent screenEvent) {
        this.screenEvent = screenEvent;
    }

    ScreenUSourceData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        screenEvent = ScreenEvent.valueOf(data);
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

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ScreenUSourceData))
            return false;
        if (!screenEvent.equals(((ScreenUSourceData) obj).screenEvent))
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

    public static final Creator<ScreenUSourceData> CREATOR
            = new Creator<ScreenUSourceData>() {
        public ScreenUSourceData createFromParcel(Parcel in) {
            return new ScreenUSourceData(in);
        }

        public ScreenUSourceData[] newArray(int size) {
            return new ScreenUSourceData[size];
        }
    };

    private ScreenUSourceData(Parcel in) {
        screenEvent = ScreenEvent.values()[in.readInt()];
    }
}
