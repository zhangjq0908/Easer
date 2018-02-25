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

package ryey.easer.core.data;

import java.util.LinkedList;
import java.util.List;

import ryey.easer.commons.plugindef.eventplugin.EventData;

/*
 * Events are linked together as trees. This class represents that structure and relevant methods.
 */
final public class EventTree {
    final EventStructure data;
    final List<EventTree> subs;
    public EventTree(EventStructure eventStructure) {
        this.data = eventStructure;
        this.subs = new LinkedList<>();
    }
    public String getName() {
        return data.name;
    }
    public ScenarioStructure getScenario() {
        return data.getScenario();
    }
    public EventData getEventData() {
        return data.getScenario().getEventData();
    }
    public boolean isReversed() {
        return data.isReverse();
    }
    public boolean isRepeatable() {
        return data.isRepeatable();
    }
    public boolean isPersistent() {
        return data.isPersistent();
    }
    public void addSub(EventTree sub) {
        subs.add(sub);
    }
    public List<EventTree> getSubs() {
        return subs;
    }
    public String getProfile() {
        return data.profileName;
    }
    public boolean isActive() {
        return data.isActive();
    }
}
