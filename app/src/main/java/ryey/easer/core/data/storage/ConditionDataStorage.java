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
import ryey.easer.core.data.ScenarioStructure;
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
        return true;
    }

    /**
     * Edit an existing {@link ConditionStructure} and handles name changing if any.
     * {@inheritDoc}
     */
    public boolean edit(String oldName, ConditionStructure condition) throws IOException {
        boolean success = super.edit(oldName, condition);
        if (success) {
            if (!oldName.equals(condition.getName())) {
                ScriptDataStorage scriptDataStorage = ScriptDataStorage.getInstance(context);
                updateScriptsForNewName(scriptDataStorage, oldName, condition);
                ScenarioDataStorage scenarioDataStorage = ScenarioDataStorage.getInstance(context);
                updateConditionEventForNewName(scenarioDataStorage, oldName, condition.getName());
            }
        }
        return success;
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

    private static void updateConditionEventForNewName(ScenarioDataStorage scenarioDataStorage, String oldName, String newName) throws IOException {
        for (String name : scenarioDataStorage.list()) {
            ScenarioStructure scenario = scenarioDataStorage.get(name);
            EventData eventData = scenario.getEventData();
            if (eventData instanceof ConditionEventEventData) {
                if (oldName.equals(((ConditionEventEventData) eventData).conditionName)) {
                    ConditionEventEventData newEventData =
                            new ConditionEventEventData((ConditionEventEventData) eventData, newName);
                    scenario.setEventData(newEventData);
                    scenarioDataStorage.update(scenario);
                }
            }
        }
    }
}
