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

package ryey.easer.core.data;

import java.util.HashMap;
import java.util.Map;

import ryey.easer.commons.EventData;

public class EventStructure {
    String name = null;
    boolean enabled = false;

    String profile = null;

    Map<String, EventData> data = new HashMap<>();

    public EventStructure() {
    }

    public EventStructure(String name) {
        this.name = name;
    }

    public boolean isValid() {
        if (name == null)
            return false;
        for (EventData d : data.values())
            if (d.isValid())
                return true;
        return false;
    }

    public EventData get(String key) {
        return data.get(key);
    }
    public void set(String key, EventData value) {
        data.put(key, value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean state) {
        enabled = state;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EventStructure)) {
            return false;
        }
        EventStructure ot = (EventStructure) o;
        if (!getName().equals(ot.getName()))
            return false;
        if (!getProfile().equals(ot.getProfile()))
            return false;
        if (!data.equals(((EventStructure) o).data))
            return false;
        return true;
    }
}
