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

package ryey.easer.core.data;

import androidx.annotation.NonNull;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.eventskill.EventData;

public class EventStructure implements Named, Verifiable, WithCreatedVersion {
    private final int createdVersion;

    private final String name;
    private EventData eventData;

    public static EventStructure createTmpScenario(@ValidData EventData eventData) {
        return new EventStructure(eventData);
    }

    private EventStructure(@ValidData EventData eventData) {
        createdVersion = C.VERSION_CREATED_IN_RUNTIME;
        name = null;
        this.eventData = eventData;
    }

    public EventStructure(int createdVersion, @NonNull String name, @ValidData EventData eventData) {
        this.createdVersion = createdVersion;
        this.name = name;
        this.eventData = eventData;
    }

    @Override
    public String getName() {
        return name;
    }

    public EventData getEventData() {
        return eventData;
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }

    public boolean isTmpEvent() {
        return name == null;
    }

    @Override
    public boolean isValid() {
        if ((name != null) && (name.isEmpty()))
            return false;
        if ((eventData == null) || (!eventData.isValid()))
            return false;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EventStructure))
            return false;
        if (!Utils.nullableEqual(name, ((EventStructure) obj).name))
            return false;
        if (!Utils.nullableEqual(eventData, ((EventStructure) obj).eventData))
            return false;
        return true;
    }

    @Override
    public int createdVersion() {
        return createdVersion;
    }
}
