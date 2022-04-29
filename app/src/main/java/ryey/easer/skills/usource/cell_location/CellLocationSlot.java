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

import android.content.Context;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import ryey.easer.skills.event.AbstractSlot;

@SuppressWarnings({"MissingPermission"})
public class CellLocationSlot extends AbstractSlot<CellLocationUSourceData> {
    private static TelephonyManager telephonyManager = null;

    private CellLocationListener cellLocationListener = new CellLocationListener();

    private CellLocationSingleData curr = null;

    public CellLocationSlot(Context context, CellLocationUSourceData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    CellLocationSlot(Context context, CellLocationUSourceData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);

        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
    }

    @Override
    public void listen() {
        if (telephonyManager != null)
            telephonyManager.listen(cellLocationListener, PhoneStateListener.LISTEN_CELL_LOCATION);
    }

    @Override
    public void cancel() {
        if (telephonyManager != null)
            telephonyManager.listen(cellLocationListener, PhoneStateListener.LISTEN_NONE);
    }

    private static Bundle dynamicsForCurrent(CellLocation location) {
        Bundle dynamics = new Bundle();
        dynamics.putString(CellLocationUSourceData.CellLocationDynamics.id, location.toString());
        return dynamics;
    }

    class CellLocationListener extends PhoneStateListener {
        @Override
        synchronized public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            curr = CellLocationSingleData.fromCellLocation(location);
            if (curr != null)
                changeSatisfiedState(eventData.data.contains(curr), dynamicsForCurrent(location));
        }
    }
}
