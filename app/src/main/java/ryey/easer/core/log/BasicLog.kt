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
import android.support.annotation.CallSuper

abstract class BasicLog: ActivityLog {
    val time: Long
    val extraInfo: String?

    constructor(time: Long, extraInfo: String? = null) {
        this.time = time
        this.extraInfo = extraInfo
    }

    final override fun time(): Long {
        return time
    }

    final override fun extraInfo(): String? {
        return extraInfo
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other == null || other !is ProfileLoadedLog)
            return false
        if (time != other.time)
            return false
        if (extraInfo != other.extraInfo) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = time.hashCode()
        result = 31 * result + (extraInfo?.hashCode() ?: 0)
        return result
    }

    protected constructor(parcel: Parcel) {
        time = parcel.readLong()
        extraInfo = parcel.readString()
    }

    @CallSuper
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(time)
        parcel.writeString(extraInfo)
    }

    override fun describeContents(): Int {
        return 0
    }
}