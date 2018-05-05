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

import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.storage.backend.ScenarioDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.scenario.JsonScenarioDataStorageBackend;

public class ScenarioDataStorage extends AbstractDataStorage<ScenarioStructure, ScenarioDataStorageBackendInterface> {


    private static ScenarioDataStorage instance = null;

    Context context;

    public static ScenarioDataStorage getInstance(Context context) {
        if (instance == null) {
            instance = new ScenarioDataStorage();
            instance.storage_backend_list = new ScenarioDataStorageBackendInterface[] {
                    JsonScenarioDataStorageBackend.getInstance(context),
            };
            instance.context = context;
        }
        return instance;
    }

    @Override
    public ScenarioStructure get(String name) {
        return super.get(name);
    }

    @Override
    boolean isSafeToDelete(String name) {
        return StorageHelper.isSafeToDeleteScenario(context, name);
    }

    @Override
    public boolean delete(String name) {
        return super.delete(name);
    }

    @Override
    public boolean edit(String oldName, ScenarioStructure scenario) throws IOException {
        if (super.edit(oldName, scenario)) {
            if (!oldName.equals(scenario.getName())) {
                handleRename(oldName, scenario);
            }
            return true;
        } else
            return false;
    }

    private void handleRename(String oldName, ScenarioStructure newScenario) throws IOException {
        ScriptDataStorage scriptDataStorage = ScriptDataStorage.getInstance(context);
        for (ScriptStructure scriptStructure : scriptDataStorage.allScripts()) {
            if (oldName.equals(scriptStructure.getScenario().getName())) {
                scriptStructure.setScenario(newScenario);
                scriptDataStorage.update(scriptStructure);
            }
        }
    }
}
