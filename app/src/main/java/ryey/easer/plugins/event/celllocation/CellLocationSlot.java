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

import android.app.PendingIntent;
import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import ryey.easer.commons.AbstractSlot;
import ryey.easer.commons.EventData;

public class CellLocationSlot extends AbstractSlot {
    static TelephonyManager telephonyManager = null;

    CellLocationListener cellLocationListener = new CellLocationListener();

    CellLocationEventData target = null;

    CellLocationEventData curr = null;

    public CellLocationSlot(Context context) {
        super(context);

        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
    }

    @Override
    public void set(EventData data) {
        if (data instanceof CellLocationEventData) {
            setStation((String) data.get());
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    public void setStation(int cid, int lac) {
        target = new CellLocationEventData(cid, lac);
    }

    public void setStation(String repr) {
        if (repr == null)
            return;
        target = new CellLocationEventData(repr);
    }

    @Override
    public boolean isValid() {
        if (!target.isValid())
            return false;
        return true;
    }

    @Override
    public void apply() {
        if (telephonyManager != null)
            telephonyManager.listen(cellLocationListener, PhoneStateListener.LISTEN_CELL_LOCATION);
    }

    @Override
    public void cancel() {
        if (telephonyManager != null)
            telephonyManager.listen(cellLocationListener, PhoneStateListener.LISTEN_NONE);
    }

    class CellLocationListener extends PhoneStateListener {
        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            curr = CellLocationEventData.fromCellLocation(location);
            if (curr.equals(target)) {
                try {
                    notifySelfIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
