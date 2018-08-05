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

class ServiceLog : BasicLog {

    val serviceName: String
    val start: Boolean

    constructor(serviceName: String, start: Boolean, extraInfo: String? = null) : super(extraInfo) {
        this.serviceName = serviceName
        this.start = start
    }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other))
            return false
        other as ServiceLog
        if (serviceName != other.serviceName)
            return false
        if (start != other.start)
            return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + serviceName.hashCode()
        result = 31 * result + start.hashCode()
        return result
    }

    constructor(parcel: Parcel) : super(parcel) {
        serviceName = parcel.readString()
        start = parcel.readByte() > 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(serviceName)
        parcel.writeByte(if (start) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ServiceLog> {
        override fun createFromParcel(parcel: Parcel): ServiceLog {
            return ServiceLog(parcel)
        }

        override fun newArray(size: Int): Array<ServiceLog?> {
            return arrayOfNulls(size)
        }
    }
}