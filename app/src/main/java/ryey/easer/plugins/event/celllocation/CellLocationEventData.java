/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.event.celllocation;

import android.telephony.CellLocation;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import java.util.List;

import ryey.easer.commons.EventData;

public class CellLocationEventData implements EventData {
    Integer cid = null;
    Integer lac = null;

    public CellLocationEventData() {}

    public CellLocationEventData(String repr) {
        set(repr);
    }

    public CellLocationEventData(int cid, int lac) {
        this.cid = cid;
        this.lac = lac;
    }

    public static CellLocationEventData fromCellLocation(CellLocation location) {
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
                return null;
            }
            return new CellLocationEventData(cid, lac);
        }
        return null;
    }

    @Override
    public Object get() {
        return toString();
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof List) {
            for (Object d : (List) obj) {
                if (!(d instanceof Integer))
                    throw new RuntimeException("illegal data");
            }
            set((List<Integer>)obj);
        } else if (obj instanceof String) {
            set((String) obj);
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    public void set(List<Integer> obj) {
        if (obj.size() != 2)
            throw new RuntimeException("illegal data");
        cid = obj.get(0);
        lac = obj.get(1);
    }

    public void set(String repr) {
        String[] parts = repr.split("-");
        cid = Integer.valueOf(parts[0]);
        lac = Integer.valueOf(parts[1]);
    }

    @Override
    public boolean isValid() {
        if (cid == null || lac == null)
            return false;
        return true;
    }

    public String toString() {
        return String.format("%d-%d", lac, cid);
    }
}
