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

package ryey.easer.plugins.operation.ui_mode;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.commons.local_plugin.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_plugin.operationplugin.OperationData;

public class UiModeOperationData implements OperationData {

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

    enum UiMode {
        car,
        normal,
    }

    UiMode ui_mode;

    UiModeOperationData(UiMode ui_mode) {
        this.ui_mode = ui_mode;
    }

    UiModeOperationData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        ui_mode = UiMode.valueOf(data);
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        return ui_mode.name();
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        return ui_mode != null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof UiModeOperationData))
            return false;
        return ui_mode == ((UiModeOperationData) obj).ui_mode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ui_mode.ordinal());
    }

    public static final Creator<UiModeOperationData> CREATOR
            = new Creator<UiModeOperationData>() {
        public UiModeOperationData createFromParcel(Parcel in) {
            return new UiModeOperationData(in);
        }

        public UiModeOperationData[] newArray(int size) {
            return new UiModeOperationData[size];
        }
    };

    private UiModeOperationData(Parcel in) {
        ui_mode = UiMode.values()[in.readInt()];
    }
}
