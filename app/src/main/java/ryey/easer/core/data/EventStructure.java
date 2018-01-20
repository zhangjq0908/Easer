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

import android.support.annotation.Nullable;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.storage.ScenarioDataStorage;

/*
 * An Event consists of the following data:
  * its name
  * the relevant data (event type is embedded in the data)
  * the profile which will be load when the event happens (nullable)
  * the parent Event of the current Event (null means no parent)
 *
 * This class holds these fields and getters and setters.
 *
 * Only `EditEventActivity` and `EventTree` needs to know the actual structure.
 */
final public class EventStructure implements Named, Verifiable {
    protected String name;
    private ScenarioStructure scenario;
    protected boolean active = true;
    private boolean reverse = false;
    private boolean repeatable = true;
    private boolean persistent = false;
    @Nullable protected String profileName;
    @Nullable protected String parentName;

    public EventStructure() {}
    public EventStructure(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScenarioStructure getScenario() {
        return scenario;
    }

    public void setScenario(ScenarioStructure scenario) {
        this.scenario = scenario;
    }

    @Deprecated
    public EventData getEventData() {
        return scenario.getEventData();
    }

    @Deprecated
    public void setEventData(EventData eventData) {
        this.scenario = ScenarioDataStorage.generateAndRecordTmpScenario(name, eventData);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public boolean isValid() {
        if ((name == null) || (name.isEmpty()))
            return false;
        if (scenario == null)
            return false;
        return true;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
