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
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import java.util.Set;

import ryey.easer.Utils;
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
final public class ScriptStructure implements Named, Verifiable, WithCreatedVersion {
    private final int createdVersion;
    @NonNull protected final String name; //TODO: make private after removing ScriptTree
    private final EventStructure event;
    private final ConditionStructure condition;
    protected final boolean active;
    private final boolean reverse;
    private final boolean repeatable;
    private final boolean persistent;
    @Nullable private final DynamicsLink dynamicsLink;
    @Nullable protected final String profileName;
    @NonNull private final Set<String> predecessors;

    public ScriptStructure(int createdVersion, @NonNull String name,
                           EventStructure event, ConditionStructure condition,
                           boolean active, boolean reverse, boolean repeatable, boolean persistent,
                           @Nullable DynamicsLink dynamicsLink, @Nullable String profileName,
                           @NonNull Set<String> predecessors) {
        this.createdVersion = createdVersion;
        this.name = name;
        this.event = event;
        this.condition = condition;
        this.active = active;
        this.reverse = reverse;
        this.repeatable = repeatable;
        this.persistent = persistent;
        this.dynamicsLink = dynamicsLink;
        this.profileName = profileName;
        this.predecessors = predecessors;
    }

    public String getName() {
        return name;
    }

    public boolean isEvent() {
        return event != null;
    }

    public EventStructure getEvent() {
        return event;
    }

    public boolean isCondition() {
        return condition != null;
    }

    public ConditionStructure getCondition() {
        return condition;
    }

    public boolean isActive() {
        return active;
    }

    @Nullable
    public String getProfileName() {
        return profileName;
    }

    @NonNull
    public Set<String> getPredecessors() {
        return predecessors;
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

    public boolean isPersistent() {
        return persistent;
    }

    public boolean isReverse() {
        return reverse;
    }

    @Override
    public int createdVersion() {
        return createdVersion;
    }

    @Nullable
    public DynamicsLink getDynamicsLink() {
        return dynamicsLink;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof ScriptStructure))
            return false;
        return createdVersion == ((ScriptStructure) obj).createdVersion
                && name.equals(((ScriptStructure) obj).name)
                && Utils.nullableEqual(event, ((ScriptStructure) obj).event)
                && Utils.nullableEqual(condition, ((ScriptStructure) obj).condition)
                && active == ((ScriptStructure) obj).active
                && reverse == ((ScriptStructure) obj).reverse
                && repeatable == ((ScriptStructure) obj).repeatable
                && persistent == ((ScriptStructure) obj).persistent
                && Utils.nullableEqual(dynamicsLink, ((ScriptStructure) obj).dynamicsLink)
                && Utils.nullableEqual(profileName, ((ScriptStructure) obj).profileName)
                && predecessors.equals(((ScriptStructure) obj).predecessors);
    }

    public Builder inBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private final int createdVersion;
        private String name;
        private EventStructure event;
        private ConditionStructure condition;
        private boolean active = true;
        private boolean reverse = false;
        private boolean repeatable = true;
        private boolean persistent = false;
        @Nullable private DynamicsLink dynamicsLink;
        @Nullable private String profileName;
        private Set<String> predecessors = new ArraySet<>();

        public Builder(int createdVersion) {
            this.createdVersion = createdVersion;
        }

        public Builder(ScriptStructure copyFrom) {
            createdVersion = copyFrom.createdVersion;
            name = copyFrom.name;
            event = copyFrom.event;
            condition = copyFrom.condition;
            active = copyFrom.active;
            reverse = copyFrom.reverse;
            repeatable = copyFrom.repeatable;
            persistent = copyFrom.persistent;
            dynamicsLink = copyFrom.dynamicsLink;
            profileName = copyFrom.profileName;
            predecessors = copyFrom.predecessors;
        }

        public ScriptStructure build() throws BuilderInfoClashedException {
            if (createdVersion < -1)
                throw new BuilderInfoClashedException("Script createdVersion shall not be < 0");
            if (name == null)
                throw new BuilderInfoClashedException("Script name shall not be null");
            if (event == null && condition == null)
                throw new BuilderInfoClashedException("Script must either have an Event or a Condition");
            if (predecessors == null)
                throw new BuilderInfoClashedException("Script predecessor set shall not be null");
            if (event != null && event.isTmpEvent())
                return new ScriptStructure(createdVersion, name, event, condition,
                        active, false, true, false,
                        dynamicsLink, profileName, predecessors);
            return new ScriptStructure(createdVersion, name, event, condition,
                    active, reverse, repeatable, persistent,
                    dynamicsLink, profileName, predecessors);
        }

        public boolean isEvent() throws BuilderInfoClashedException {
            if ((event == null) == (condition == null))
                throw new BuilderInfoClashedException("A Script should either has an Event or a Condition when calling Builder.isEvent()");
            return event != null;
        }

        public boolean isTmpEvent() throws BuilderInfoClashedException {
            return isEvent() && event.isTmpEvent();
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        @Deprecated
        public Builder setTmpEvent(EventData eventData) {
            this.event = EventStructure.createTmpScenario(eventData);
            return this;
        }

        public Builder setEvent(EventStructure event) {
            this.event = event;
            return this;
        }

        public Builder setCondition(ConditionStructure condition) {
            this.condition = condition;
            return this;
        }

        public Builder setActive(boolean active) {
            this.active = active;
            return this;
        }

        public Builder setReverse(boolean reverse) {
            this.reverse = reverse;
            return this;
        }

        public Builder setRepeatable(boolean repeatable) {
            this.repeatable = repeatable;
            return this;
        }

        public Builder setPersistent(boolean persistent) {
            this.persistent = persistent;
            return this;
        }

        public Builder setDynamicsLink(@Nullable DynamicsLink dynamicsLink) {
            this.dynamicsLink = dynamicsLink;
            return this;
        }

        public Builder setProfileName(@Nullable String profileName) {
            this.profileName = profileName;
            return this;
        }

        public Builder setPredecessors(@NonNull Set<String> predecessors) {
            this.predecessors = predecessors;
            return this;
        }

        public Builder addPredecessor(@NonNull String predecessor) {
            this.predecessors.add(predecessor);
            return this;
        }

        public Builder removePredecessor(@NonNull String predecessor) {
            this.predecessors.remove(predecessor);
            return this;
        }
    }
}
