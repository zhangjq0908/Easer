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

class ScriptSatisfactionLog : BasicLog {

    val scriptName: String
    val satisfaction: Boolean
    val profileName: String?

    constructor(time: Long, scriptName: String, satisfaction: Boolean = true, profileName: String?, extraInfo: String?)
            : super(time, extraInfo) {
        this.scriptName = scriptName
        this.satisfaction = satisfaction
        this.profileName = profileName
    }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other))
            return false
        other as ScriptSatisfactionLog
        if (!Utils.nullableEqual(scriptName, other.scriptName))
            return false
        if (satisfaction != satisfaction)
            return false
        if (!Utils.nullableEqual(profileName, other.profileName))
            return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + scriptName.hashCode()
        result = 31 * result + satisfaction.hashCode()
        result = 31 * result + (profileName?.hashCode() ?: 0)
        return result
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        super.writeToParcel(parcel, i)
        parcel.writeString(scriptName)
        parcel.writeByte(if (satisfaction) 1 else 0)
        parcel.writeString(profileName)
    }

    protected constructor(parcel: Parcel): super(parcel) {
        scriptName = parcel.readString()
        satisfaction = parcel.readByte() > 0
        profileName = parcel.readString()
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
