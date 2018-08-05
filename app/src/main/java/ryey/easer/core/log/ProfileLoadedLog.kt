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

class ProfileLoadedLog : BasicLog {
    val profileName: String

    constructor(profile: String, extraInfo: String? = null) : super(extraInfo) {
        this.profileName = profile
    }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other))
            return false
        other as ProfileLoadedLog
        if (!Utils.nullableEqual(profileName, other.profileName))
            return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + profileName.hashCode()
        return result
    }

    constructor(parcel: Parcel) : super(parcel) {
        profileName = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(profileName)
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