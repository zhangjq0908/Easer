/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import ryey.easer.Utils;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public class CellLocationSlot extends AbstractSlot<CellLocationEventData> {
    private static TelephonyManager telephonyManager = null;

    private CellLocationListener cellLocationListener = new CellLocationListener();

    private EventType type = null;

    private CellLocationSingleData curr = null;

    public CellLocationSlot(Context context, CellLocationEventData data) {
        super(context, data);
        type = data.type();

        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
    }

    @Override
    public boolean isValid() {
        if (!eventData.isValid())
            return false;
        return true;
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

    @SuppressLint("MissingPermission")
    @Override
    public void check() {
        if (!Utils.hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION))
            return;
        CellLocationListener chck = new CellLocationListener();
        chck.onCellLocationChanged(telephonyManager.getCellLocation());
    }

    class CellLocationListener extends PhoneStateListener {
        @Override
        synchronized public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            curr = CellLocationSingleData.fromCellLocation(location);
            if (type == EventType.any) {
                changeSatisfiedState(eventData.contains(curr));
            } else if (type == EventType.none) {
                changeSatisfiedState(!eventData.contains(curr));
            }
        }
    }
}
