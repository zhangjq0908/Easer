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

package ryey.easer.remote_plugin;

import android.os.Parcel;
import android.os.Parcelable;

import ryey.easer.commons.PluginDataFormat;

public class RemoteOperationData implements Parcelable {

    public final String pluginId;
    public final PluginDataFormat format;
    public final String rawData;

    public RemoteOperationData(String pluginId, PluginDataFormat format, String rawData) {
        this.pluginId = pluginId;
        this.format = format;
        this.rawData = rawData;
    }

    protected RemoteOperationData(Parcel in) {
        pluginId = in.readString();
        format = PluginDataFormat.fromParcel(in);
        rawData = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pluginId);
        PluginDataFormat.toParcel(dest, format);
        dest.writeString(rawData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RemoteOperationData> CREATOR = new Creator<RemoteOperationData>() {
        @Override
        public RemoteOperationData createFromParcel(Parcel in) {
            return new RemoteOperationData(in);
        }

        @Override
        public RemoteOperationData[] newArray(int size) {
            return new RemoteOperationData[size];
        }
    };
}
