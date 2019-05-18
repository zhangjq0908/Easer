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

package ryey.easer.core.data.storage;

import android.content.Context;

import java.io.IOException;

import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.storage.backend.ConditionDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.condition.JsonConditionDataStorageBackend;
import ryey.easer.skills.event.condition_event.ConditionEventEventData;

public class ConditionDataStorage extends AbstractDataStorage<ConditionStructure, ConditionDataStorageBackendInterface> {

    public ConditionDataStorage(Context context) {
        super(context, new ConditionDataStorageBackendInterface[] {
                new JsonConditionDataStorageBackend(context),
        });
    }

    @Override
    boolean isSafeToDelete(String name) {
        ScriptDataStorage scriptDataStorage = new ScriptDataStorage(context);
        for (String scriptName : scriptDataStorage.list()) {
            ScriptStructure script = scriptDataStorage.get(scriptName);
            if (script.isCondition()) {
                if (script.getCondition().getName().equals(name)) {
                    return false;
                }
            }
        }
        EventDataStorage eventDataStorage = new EventDataStorage(context);
        for (String scenarioName : eventDataStorage.list()) {
            EventStructure scenario = eventDataStorage.get(scenarioName);
            EventData eventData = scenario.getEventData();
            if (eventData instanceof ConditionEventEventData) {
                if (name.equals(((ConditionEventEventData) eventData).conditionName)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void handleRename(String oldName, ConditionStructure condition) throws IOException {
        ScriptDataStorage scriptDataStorage = new ScriptDataStorage(context);
        updateScriptsForNewName(scriptDataStorage, oldName, condition);
        EventDataStorage eventDataStorage = new EventDataStorage(context);
        updateConditionEventForNewName(eventDataStorage, oldName, condition.getName());
        updateInlineConditionEventForNewName(scriptDataStorage, oldName, condition.getName());
    }

    private static void updateScriptsForNewName(ScriptDataStorage scriptDataStorage, String oldName, ConditionStructure condition) throws IOException {
        for (String name : scriptDataStorage.list()) {
            ScriptStructure script = scriptDataStorage.get(name);
            if (script.isCondition()) {
                if (script.getCondition().getName().equals(oldName)) {
                    script.setCondition(condition);
                    scriptDataStorage.update(script);
                }
            }
        }
    }

    private static void updateConditionEventForNewName(EventDataStorage eventDataStorage, String oldName, String newName) throws IOException {
        for (String name : eventDataStorage.list()) {
            EventStructure event = eventDataStorage.get(name);
            EventData eventData = event.getEventData();
            if (eventData instanceof ConditionEventEventData) {
                if (oldName.equals(((ConditionEventEventData) eventData).conditionName)) {
                    ConditionEventEventData newEventData =
                            new ConditionEventEventData((ConditionEventEventData) eventData, newName);
                    event.setEventData(newEventData);
                    eventDataStorage.update(event);
                }
            }
        }
    }

    private static void updateInlineConditionEventForNewName(ScriptDataStorage scriptDataStorage, String oldName, String newName) throws IOException {
        for (String scriptName : scriptDataStorage.list()) {
            ScriptStructure script = scriptDataStorage.get(scriptName);
            if (script.isEvent()) {
                EventStructure event = script.getEvent();
                if (event.isTmpEvent()) {
                    EventData eventData = event.getEventData();
                    if (eventData instanceof ConditionEventEventData) {
                        ConditionEventEventData newEventData =
                                new ConditionEventEventData((ConditionEventEventData) eventData, newName);
                        event.setEventData(newEventData);
                        script.setEvent(event);
                        scriptDataStorage.update(script);
                    }
                }
            }
        }
    }
}
