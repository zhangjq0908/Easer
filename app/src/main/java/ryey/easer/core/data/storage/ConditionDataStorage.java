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

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.storage.backend.ConditionDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.condition.JsonConditionDataStorageBackend;
import ryey.easer.plugins.event.condition_event.ConditionEventEventData;

public class ConditionDataStorage extends AbstractDataStorage<ConditionStructure, ConditionDataStorageBackendInterface> {

    private static ConditionDataStorage instance = null;

    private Context context;

    public static ConditionDataStorage getInstance(Context context) {
        if (instance == null) {
            instance = new ConditionDataStorage();
            instance.storage_backend_list = new ConditionDataStorageBackendInterface[] {
                    JsonConditionDataStorageBackend.getInstance(context),
            };
            instance.context = context;
        }
        return instance;
    }

    @Override
    boolean isSafeToDelete(String name) {
        ScriptDataStorage scriptDataStorage = ScriptDataStorage.getInstance(context);
        for (String scriptName : scriptDataStorage.list()) {
            ScriptStructure script = scriptDataStorage.get(scriptName);
            if (script.isCondition()) {
                if (script.getCondition().getName().equals(name)) {
                    return false;
                }
            }
        }
        EventDataStorage eventDataStorage = EventDataStorage.getInstance(context);
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
        ScriptDataStorage scriptDataStorage = ScriptDataStorage.getInstance(context);
        updateScriptsForNewName(scriptDataStorage, oldName, condition);
        EventDataStorage eventDataStorage = EventDataStorage.getInstance(context);
        updateConditionEventForNewName(eventDataStorage, oldName, condition.getName());
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
            EventStructure scenario = eventDataStorage.get(name);
            EventData eventData = scenario.getEventData();
            if (eventData instanceof ConditionEventEventData) {
                if (oldName.equals(((ConditionEventEventData) eventData).conditionName)) {
                    ConditionEventEventData newEventData =
                            new ConditionEventEventData((ConditionEventEventData) eventData, newName);
                    scenario.setEventData(newEventData);
                    eventDataStorage.update(scenario);
                }
            }
        }
    }
}
