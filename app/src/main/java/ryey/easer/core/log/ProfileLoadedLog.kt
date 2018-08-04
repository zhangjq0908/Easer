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

package ryey.easer.core.log

import android.os.Parcel
import android.os.Parcelable
import ryey.easer.Utils

class ProfileLoadedLog : ActivityLog {
    val time: Long
    val profileName: String
    val extraInfo: String?

    constructor(time: Long, profile: String, extraInfo: String? = null) {
        this.time = time
        this.profileName = profile
        this.extraInfo = extraInfo
    }

    override fun time(): Long {
        return time
    }

    override fun extraInfo(): String? {
        return extraInfo
    }

    override fun equals(obj: Any?): Boolean {
        if (obj === this)
            return true
        if (obj == null || obj !is ProfileLoadedLog)
            return false
        if (!Utils.nullableEqual(profileName, obj.profileName))
            return false
        if (time != obj.time)
            return false
        return true
    }

    constructor(parcel: Parcel) {
        time = parcel.readLong()
        profileName = parcel.readString()
        extraInfo = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(time)
        parcel.writeString(profileName)
        parcel.writeString(extraInfo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileLoadedLog> {
        override fun createFromParcel(parcel: Parcel): ProfileLoadedLog {
            return ProfileLoadedLog(parcel)
        }

        override fun newArray(size: Int): Array<ProfileLoadedLog?> {
            return arrayOfNulls(size)
        }
    }
}