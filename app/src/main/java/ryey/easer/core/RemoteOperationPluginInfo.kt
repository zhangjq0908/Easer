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

package ryey.easer.core

import android.os.Parcel
import android.os.Parcelable
import ryey.easer.plugin.operation.Category
import ryey.easer.remote_plugin.RemotePlugin

class RemoteOperationPluginInfo(packageName: String, pluginId: String,
                                pluginName: String, activityEditData: String?, val category: Category)
    : RemotePluginInfo(packageName, pluginId, pluginName, RemotePlugin.TYPE_OPERATION_PLUGIN, activityEditData) {

    constructor(packageName: String, pluginId: String,
                pluginName: String, activityEditData: String?, category: String)
            : this(packageName, pluginId, pluginName, activityEditData,
            try {
                Category.valueOf(category)
            } catch (e: Exception) {
                Category.unknown
            })

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(packageName)
        parcel.writeString(pluginId)
        parcel.writeString(pluginName)
        parcel.writeString(activityEditData)
        parcel.writeString(category.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RemoteOperationPluginInfo> {
        override fun createFromParcel(parcel: Parcel): RemoteOperationPluginInfo {
            return RemoteOperationPluginInfo(parcel)
        }

        override fun newArray(size: Int): Array<RemoteOperationPluginInfo?> {
            return arrayOfNulls(size)
        }
    }
}