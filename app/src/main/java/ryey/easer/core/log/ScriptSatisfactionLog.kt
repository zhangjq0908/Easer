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

class ScriptSatisfactionLog : ActivityLog {

    val time: Long
    val scriptName: String
    val satisfaction: Boolean
    val profileName: String?
    val extraInfo: String?

    constructor(time: Long, scriptName: String, satisfaction: Boolean = true, profileName: String?, extraInfo: String?) {
        this.time = time
        this.scriptName = scriptName
        this.satisfaction =satisfaction
        this.profileName = profileName
        this.extraInfo = extraInfo
    }

    override fun time(): Long {
        return time
    }

    override fun extraInfo(): String? {
        return extraInfo
    }

    @Suppress("RedundantIf")
    override fun equals(obj: Any?): Boolean {
        if (obj === this)
            return true
        if (obj == null || obj !is ScriptSatisfactionLog)
            return false
        if (!Utils.nullableEqual(scriptName, obj.scriptName))
            return false
        if (!Utils.nullableEqual(profileName, obj.profileName))
            return false
        if (time != obj.time)
            return false
        return true
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeLong(time)
        parcel.writeString(scriptName)
        parcel.writeByte(if (satisfaction) 1 else 0)
        parcel.writeString(profileName)
        parcel.writeString(extraInfo)
    }

    protected constructor(parcel: Parcel) {
        time = parcel.readLong()
        scriptName = parcel.readString()
        satisfaction = parcel.readByte() > 0
        profileName = parcel.readString()
        extraInfo = parcel.readString()
    }

    companion object CREATOR : Parcelable.Creator<ScriptSatisfactionLog> {
        override fun createFromParcel(parcel: Parcel): ScriptSatisfactionLog {
            return ScriptSatisfactionLog(parcel)
        }

        override fun newArray(size: Int): Array<ScriptSatisfactionLog?> {
            return arrayOfNulls(size)
        }
    }
}
