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

package ryey.easer.skills.usource.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import ryey.easer.skills.event.AbstractSlot;

public class LocationSlot extends AbstractSlot<LocationUSourceData> {

    private final LocationManager locationManager;
    private final Criteria criteria;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (LocationUtils.isAcceptable(eventData, location)) {
                if (LocationUtils.isInside(eventData, location)) {
                    changeSatisfiedState(true);
                } else {
                    changeSatisfiedState(false);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    LocationSlot(Context context, LocationUSourceData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    LocationSlot(Context context, LocationUSourceData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        criteria = LocationUtils.getCriteria();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void listen() {
        locationManager.requestLocationUpdates(
                eventData.listenMinTime * LocationUSourceData.UNIT_LISTEN_MIN_TIME, eventData.radius,
                criteria, locationListener, Looper.myLooper());
    }

    @Override
    public void cancel() {
        locationManager.removeUpdates(locationListener);
    }

}
