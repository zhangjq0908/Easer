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

package ryey.easer.plugins.event;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.Set;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public abstract class TypedEventData implements EventData {
    protected Set<EventType> availableTypes;
    protected EventType default_type;
    private EventType type = null;

    @Override
    public void setType(@NonNull EventType type) {
        if (!isAvailable(type)) {
            Logger.e("Attempt to set improper EventType (available [%s], attempt [%s])", availableTypes(), type);
            throw new IllegalArgumentException("Improper EventType to set");
        }
        this.type = type;
    }

    @NonNull
    @Override
    public EventType type() {
        if (type != null)
            return type;
        else
            return default_type;
    }

    @NonNull
    @Override
    public Set<EventType> availableTypes() {
        return availableTypes;
    }

    @Override
    public boolean isAvailable(@NonNull EventType type) {
        return availableTypes().contains(type);
    }

    @Override
    public boolean match(@NonNull Object obj) {
        return equals(obj);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TypedEventData))
            return false;
        if (type() != ((TypedEventData) obj).type())
            return false;
        return true;
    }
}
