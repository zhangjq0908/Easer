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

package ryey.easer.skills.usource.cell_location;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Locale;

class CellLocationSingleData implements Parcelable {
    private Integer cid = null;
    private Integer lac = null;

    @Nullable
    static CellLocationSingleData fromCellLocation(@Nullable CellLocation location) {
        int cid, lac;
        if (location != null) {
            if (location instanceof GsmCellLocation) {
                cid = ((GsmCellLocation) location).getCid();
                lac = ((GsmCellLocation) location).getLac();
            }
            else if (location instanceof CdmaCellLocation) {
                cid = ((CdmaCellLocation) location).getBaseStationId();
                lac = ((CdmaCellLocation) location).getSystemId();
            } else {
                //TODO: More
                return null;
            }
            return new CellLocationSingleData(cid, lac);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    static CellLocationSingleData fromCellInfo(@NonNull CellInfo cellInfo) {
        int cid, lac;
        if (cellInfo instanceof CellInfoGsm) {
            CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
            cid = cellIdentity.getCid();
            lac = cellIdentity.getLac();
        } else if (cellInfo instanceof CellInfoCdma) {
            CellIdentityCdma cellIdentity = ((CellInfoCdma) cellInfo).getCellIdentity();
            cid = cellIdentity.getBasestationId();
            lac = cellIdentity.getSystemId();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (cellInfo instanceof CellInfoWcdma) {
                CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                cid = cellIdentity.getCid();
                lac = cellIdentity.getLac();
            } else {
                //TODO: More
                return null;
            }
        } else
            return null;
        return new CellLocationSingleData(cid, lac);
    }

    CellLocationSingleData() {}

    private CellLocationSingleData(int cid, int lac) {
        this.cid = cid;
        this.lac = lac;
    }

    public void set(Object obj) {
        if (obj instanceof List) {
            for (Object d : (List) obj) {
                if (!(d instanceof Integer))
                    throw new RuntimeException("illegal data");
            }
            //noinspection unchecked (it is actually checked!)
            set((List<Integer>) obj);
        } else if (obj instanceof String) {
            set((String) obj);
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    private void set(List<Integer> obj) {
        if (obj.size() != 2)
            throw new RuntimeException("illegal data");
        cid = obj.get(0);
        lac = obj.get(1);
    }

    private void set(String repr) {
        String[] parts = repr.split("-");
        if (parts.length != 2)
            return;
        cid = Integer.valueOf(parts[0].trim());
        lac = Integer.valueOf(parts[1].trim());
    }

    public boolean isValid() {
        if (cid == null || lac == null)
            return false;
        return true;
    }

    @NonNull
    public String toString() {
        return String.format(Locale.US, "%d-%d", cid, lac);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellLocationSingleData that = (CellLocationSingleData) o;

        if (cid != null ? !cid.equals(that.cid) : that.cid != null) return false;
        return lac != null ? lac.equals(that.lac) : that.lac == null;

    }

    @Override
    public int hashCode() {
        int result = cid != null ? cid.hashCode() : 0;
        result = 31 * result + (lac != null ? lac.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cid);
        dest.writeInt(lac);
    }

    public static final Creator<CellLocationSingleData> CREATOR
            = new Creator<CellLocationSingleData>() {
        public CellLocationSingleData createFromParcel(Parcel in) {
            return new CellLocationSingleData(in);
        }

        public CellLocationSingleData[] newArray(int size) {
            return new CellLocationSingleData[size];
        }
    };

    private CellLocationSingleData(Parcel in) {
        cid = in.readInt();
        lac = in.readInt();
    }

}
