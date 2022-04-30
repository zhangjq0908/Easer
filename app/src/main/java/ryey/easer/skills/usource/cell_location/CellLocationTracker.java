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

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.skills.condition.SkeletonTracker;

@SuppressWarnings({"MissingPermission"})
public class CellLocationTracker extends SkeletonTracker<CellLocationUSourceData> {

    private static TelephonyManager telephonyManager = null;

    private CellLocationListener cellLocationListener = new CellLocationListener();

    @SuppressLint("MissingPermission")
    CellLocationTracker(Context context, CellLocationUSourceData data,
                        @NonNull PendingIntent event_positive,
                        @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);

        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        newSatisfiedState(match(telephonyManager.getCellLocation()));
    }

    @Override
    public void start() {
        telephonyManager.listen(cellLocationListener, PhoneStateListener.LISTEN_CELL_LOCATION);
    }

    @Override
    public void stop() {
        telephonyManager.listen(cellLocationListener, PhoneStateListener.LISTEN_NONE);
    }

    @Nullable
    private Boolean match(CellLocation location) {
        CellLocationSingleData curr = CellLocationSingleData.fromCellLocation(location);
        if (curr == null)
            return null;
        return data.data.contains(curr);
    }

    class CellLocationListener extends PhoneStateListener {
        @Override
        synchronized public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            newSatisfiedState(match(location));
        }
    }
}
