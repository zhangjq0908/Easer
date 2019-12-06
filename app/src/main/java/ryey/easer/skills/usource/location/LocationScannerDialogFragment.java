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

package ryey.easer.skills.usource.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.skills.usource.ScannerDialogFragment;

public class LocationScannerDialogFragment extends ScannerDialogFragment<LocationCandidate> {

    private static final int LISTEN_MIN_TIME = 1 * 1000; // 1s
    private static final int LISTEN_MIN_DISTANCE = 10; // 10m
    private static final Criteria criteria = LocationUtils.getCriteria();

    private LocationManager locationManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(
                LISTEN_MIN_TIME, LISTEN_MIN_DISTANCE,
                criteria, locationListener, Looper.myLooper());
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        locationManager = null;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            addData(LocationCandidate.Companion.fromLocation(location));
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

    @Nullable
    @Override
    protected OnListItemClickedListener<LocationCandidate> listItemClickedListener() {
        return (OnListItemClickedListener<LocationCandidate>) getTargetFragment();
    }
}
