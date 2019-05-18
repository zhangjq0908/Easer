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

package ryey.easer.commons.local_skill.dynamics;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class DynamicsLink implements Parcelable {
    private HashMap<String, String> link = new HashMap<>();

    public DynamicsLink() {

    }

    public void put(String placeholder, String property) {
        link.put(placeholder, property);
    }

    @NonNull
    public Map<String, String> identityMap() {
        return link;
    }

    /**
     * From `A ==> B & B ==> C` to `A ==> C`
     * link is `A ==> B` (Placeholder ==> Dynamics id)
     * extras is `B ==> C` (Dynamics id ==> Dynamics value)
     * @param extras The mapping between Dynamics id to Dynamics value
     * @return Mapping from Placeholder to Dynamics value
     */
    @NonNull
    public SolidDynamicsAssignment assign(Bundle extras) {
        HashMap<String, String> assignment = new HashMap<>();
        for (String key : link.keySet()) {
            String dynamics_id = link.get(key);
            String dynamics_value = extras.getString(dynamics_id);
            if (dynamics_value != null) {
                assignment.put(key, dynamics_value);
            }
        }
        return new SolidDynamicsAssignment(assignment);
    }

    private DynamicsLink(Parcel in) {
        in.readMap(link, HashMap.class.getClassLoader());
    }

    public static final Parcelable.Creator<DynamicsLink> CREATOR = new Parcelable.Creator<DynamicsLink>() {
        @Override
        public DynamicsLink createFromParcel(Parcel in) {
            return new DynamicsLink(in);
        }

        @Override
        public DynamicsLink[] newArray(int size) {
            return new DynamicsLink[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeMap(link);
    }

}
