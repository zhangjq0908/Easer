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

package ryey.easer.core.data;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import ryey.easer.commons.local_plugin.eventplugin.EventData;

/*
 * Events are linked together as trees. This class represents that structure and relevant methods.
 *
 * TODO: Remove delegations which don't appear to be relevant to the "script tree" (but rather, related to the exact Script when triggered).
 */
final public class ScriptTree {
    final ScriptStructure data;
    @NonNull final List<ScriptTree> subs;
    public ScriptTree(@NonNull ScriptStructure scriptStructure) {
        this.data = scriptStructure;
        this.subs = new LinkedList<>();
    }
    public ScriptTree(@NonNull ScriptStructure data, @NonNull List<ScriptTree> subs) {
        this.data = data;
        this.subs = subs;
    }
    public String getName() {
        return data.name;
    }
    public boolean isEvent() {
        return data.getEvent() != null;
    }
    public boolean isCondition() {
        return data.getCondition() != null;
    }
    public EventStructure getEvent() {
        return data.getEvent();
    }
    public ConditionStructure getCondition() {
        return data.getCondition();
    }
    public EventData getEventData() {
        return data.getEvent().getEventData();
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
    public void addSub(ScriptTree sub) {
        subs.add(sub);
    }
    @NonNull
    public List<ScriptTree> getSubs() {
        return subs;
    }
    public String getProfile() {
        return data.profileName;
    }
    public boolean isActive() {
        return data.isActive();
    }

    public ScriptStructure getData() {
        return data;
    }
}
