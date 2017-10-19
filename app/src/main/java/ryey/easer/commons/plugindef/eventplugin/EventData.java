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

package ryey.easer.commons.plugindef.eventplugin;

import java.util.Set;

import ryey.easer.commons.plugindef.StorageData;

public interface EventData extends StorageData {

    /**
     * EventType related methods
     */

    // Setter
    void setType(EventType type);
    // Getter
    EventType type();
    /**
     * List all available (i.e. can be handled) types of this plugin
     */
    Set<EventType> availableTypes();
    /**
     * Check to see if such a type is available
     * FIXME: Designed to be used when dealing with storage
     */
    boolean isAvailable(EventType type);

    /**
     * Test whether the obj matches any of the data
     * Used when the data could contain multiple choices (e.g. when using type `any`)
     */
    boolean match(Object obj);
}
