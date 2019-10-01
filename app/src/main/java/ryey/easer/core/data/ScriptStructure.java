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

import androidx.annotation.Nullable;

import ryey.easer.commons.local_skill.dynamics.DynamicsLink;
import ryey.easer.commons.local_skill.eventskill.EventData;

/*
 * An Event consists of the following data:
  * its name
  * the relevant data (event type is embedded in the data)
  * the profile which will be load when the event happens (nullable)
  * the parent Event of the current Event (null means no parent)
 *
 * This class holds these fields and getters and setters.
 *
 * Only `EditScriptActivity` and `LogicGraph` needs to know the actual structure (?)
 */
final public class ScriptStructure implements Renameable, Verifiable, WithCreatedVersion {
    private final int createdVersion;
    protected String name;
    private EventStructure event;
    private ConditionStructure condition;
    protected boolean active = true;
    private boolean reverse = false;
    private boolean repeatable = true;
    private boolean persistent = false;
    @Nullable private DynamicsLink dynamicsLink;
    @Nullable protected String profileName;
    @Nullable protected String parentName;

    public ScriptStructure(int createdVersion) {this.createdVersion = createdVersion;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEvent() {
        return event != null;
    }

    public EventStructure getEvent() {
        return event;
    }

    public void setEvent(EventStructure event) {
        this.event = event;
    }

    public boolean isCondition() {
        return condition != null;
    }

    public ConditionStructure getCondition() {
        return condition;
    }

    public void setCondition(ConditionStructure condition) {
        this.condition = condition;
    }

    @Deprecated
    public EventData getEventData() {
        return event.getEventData();
    }

    @Deprecated
    public void setEventData(EventData eventData) {
        this.event = EventStructure.createTmpScenario(eventData);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Nullable
    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(@Nullable String profileName) {
        this.profileName = profileName;
    }

    @Nullable
    public String getParentName() {
        return parentName;
    }

    public void setParentName(@Nullable String parentName) {
        this.parentName = parentName;
    }

    public boolean isValid() {
        if ((name == null) || (name.isEmpty()))
            return false;
        if ((event == null) == (condition == null))
            return false;
        if (event != null && !event.isValid())
            return false;
        if (condition != null && !condition.isValid())
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

    @Override
    public int createdVersion() {
        return createdVersion;
    }

    public void setDynamicsLink(@Nullable DynamicsLink dynamicsLink) {
        this.dynamicsLink = dynamicsLink;
    }

    @Nullable
    public DynamicsLink getDynamicsLink() {
        return dynamicsLink;
    }
}
