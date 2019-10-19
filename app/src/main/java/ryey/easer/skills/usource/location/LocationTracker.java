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
import android.app.PendingIntent;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;

import ryey.easer.skills.condition.SkeletonTracker;

public class LocationTracker extends SkeletonTracker<LocationUSourceData> {

    private final LocationManager locationManager;
    private final Criteria criteria;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (LocationUtils.isAcceptable(data, location)) {
                if (LocationUtils.isInside(data, location)) {
                    newSatisfiedState(true);
                } else {
                    newSatisfiedState(false);
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

    LocationTracker(Context context, LocationUSourceData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        criteria = LocationUtils.getCriteria();

        @SuppressLint("MissingPermission") Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (LocationUtils.isAcceptable(data, lastKnownLocation)) {
            newSatisfiedState(LocationUtils.isInside(data, lastKnownLocation));
        } else {
            @SuppressLint("MissingPermission") Location lastKnownLocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (LocationUtils.isAcceptable(data, lastKnownLocationGps)) {
                newSatisfiedState(LocationUtils.isInside(data, lastKnownLocationGps));
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void start() {
        locationManager.requestLocationUpdates(
                data.listenMinTime * LocationUSourceData.UNIT_LISTEN_MIN_TIME, data.radius,
                criteria, locationListener, Looper.myLooper());
    }

    @Override
    public void stop() {
        locationManager.removeUpdates(locationListener);
    }

}
