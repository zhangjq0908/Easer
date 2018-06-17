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
import java.util.Collection;
import java.util.List;

import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.ScriptTree;
import ryey.easer.core.data.storage.backend.ScriptDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.script.JsonScriptDataStorageBackend;
import ryey.easer.plugins.operation.state_control.StateControlOperationData;
import ryey.easer.plugins.operation.state_control.StateControlOperationPlugin;

public class ScriptDataStorage extends AbstractDataStorage<ScriptStructure, ScriptDataStorageBackendInterface> {

    private static ScriptDataStorage instance = null;

    private final Context context;

    public static ScriptDataStorage getInstance(Context context) {
        if (instance == null) {
            instance = new ScriptDataStorage(context);
            instance.storage_backend_list = new ScriptDataStorageBackendInterface[] {
                    JsonScriptDataStorageBackend.getInstance(context),
            };
        }
        return instance;
    }

    private ScriptDataStorage(Context context) {
        this.context = context;
    }

    @Override
    boolean isSafeToDelete(String name) {
        for (ScriptStructure scriptStructure : allScripts()) {
            if (name.equals(scriptStructure.getParentName()))
                return false;
        }
        ProfileDataStorage profileDataStorage = ProfileDataStorage.getInstance(context);
        String s_id = (new StateControlOperationPlugin()).id();
        for (String pname : profileDataStorage.list()) {
            ProfileStructure profile = profileDataStorage.get(pname);
            Collection<OperationData> dataCollection = profile.get(s_id);
            if (dataCollection != null) {
                for (OperationData operationData : dataCollection) {
                    StateControlOperationData stateControlOperationData = (StateControlOperationData) operationData;
                    if (name.equals(stateControlOperationData.scriptName))
                        return false;
                }
            }
        }
        return true;
    }

    public List<ScriptTree> getScriptTrees() {
        return StorageHelper.eventListToTrees(allScripts());
    }

    List<ScriptStructure> allScripts() {
        List<ScriptStructure> list = null;
        for (ScriptDataStorageBackendInterface backend : storage_backend_list) {
            if (list == null)
                list = backend.all();
            else
                list.addAll(backend.all());
        }
        return list;
    }

    @Override
    protected void handleRename(String oldName, ScriptStructure script) throws IOException {
        // alter subnodes to point to the new name
        List<ScriptStructure> subs = StorageHelper.scriptParentMap(allScripts()).get(oldName);
        if (subs != null) {
            for (ScriptStructure sub : subs) {
                sub.setParentName(script.getName());
                update(sub);
            }
        }
    }

}
