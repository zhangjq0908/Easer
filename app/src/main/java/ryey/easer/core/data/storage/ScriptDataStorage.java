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

package ryey.easer.core.data.storage;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.core.data.LogicGraph;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.RemoteLocalOperationDataWrapper;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.ScriptTree;
import ryey.easer.core.data.storage.backend.ScriptDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.script.JsonScriptDataStorageBackend;
import ryey.easer.skills.operation.state_control.StateControlOperationData;
import ryey.easer.skills.operation.state_control.StateControlOperationSkill;

public class ScriptDataStorage extends AbstractDataStorage<ScriptStructure, ScriptDataStorageBackendInterface> {

    public ScriptDataStorage(Context context) {
        super(context, new ScriptDataStorageBackendInterface[] {
            new JsonScriptDataStorageBackend(context),
        });
    }

    @Override
    boolean isSafeToDelete(String name) {
        for (ScriptStructure scriptStructure : allScripts()) {
            if (name.equals(scriptStructure.getParentName()))
                return false;
        }
        ProfileDataStorage profileDataStorage = new ProfileDataStorage(context);
        String s_id = (new StateControlOperationSkill()).id();
        for (String pname : profileDataStorage.list()) {
            ProfileStructure profile = profileDataStorage.get(pname);
            Collection<RemoteLocalOperationDataWrapper> dataCollection = profile.get(s_id);
            if (dataCollection != null) {
                for (RemoteLocalOperationDataWrapper dataWrapper : dataCollection) {
                    if (dataWrapper.isRemote())
                        throw new IllegalStateException("StateControlOperationData should not be remote");
                    StateControlOperationData stateControlOperationData = (StateControlOperationData) dataWrapper.localData;
                    assert stateControlOperationData != null;
                    if (name.equals(stateControlOperationData.scriptName))
                        return false;
                }
            }
        }
        return true;
    }

    @Deprecated
    public List<ScriptTree> getScriptTrees() {
        return StorageHelper.logicGraphToTreeList(getLogicGraph());
    }

    public LogicGraph getLogicGraph() {
        return LogicGraph.createFromScriptList(allScripts());
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
        String name = script.getName();
        // alter subnodes to point to the new name
        List<ScriptStructure> subs = StorageHelper.scriptParentMap(allScripts()).get(oldName);
        if (subs != null) {
            for (ScriptStructure sub : subs) {
                sub.setParentName(name);
                update(sub);
            }
        }
        ProfileDataStorage profileDataStorage = new ProfileDataStorage(context);
        String s_id = (new StateControlOperationSkill()).id();
        for (String pname : profileDataStorage.list()) {
            ProfileStructure profile = profileDataStorage.get(pname);
            Collection<RemoteLocalOperationDataWrapper> dataCollection = profile.get(s_id);
            if (dataCollection != null) {
                List<StateControlOperationData> replaceData = new ArrayList<>();
                for (RemoteLocalOperationDataWrapper operationData : dataCollection) {
                    if (operationData.isRemote())
                        throw new IllegalStateException("StateControlOperationData should not be remote");
                    OperationData localData = operationData.localData;
                    assert localData != null;
                    StateControlOperationData stateControlOperationData = (StateControlOperationData) localData;
                    if (oldName.equals(stateControlOperationData.scriptName)) {
                        replaceData.add(stateControlOperationData);
                    }
                }
                if (replaceData.size() > 0) {
                    Collection<OperationData> newDataCollection = new ArrayList<>(dataCollection.size());
                    for (RemoteLocalOperationDataWrapper wrapper : dataCollection) {
                        newDataCollection.add(wrapper.localData);
                    }
                    for (StateControlOperationData operationData : replaceData) {
                        newDataCollection.remove(operationData);
                        StateControlOperationData newData = new StateControlOperationData(operationData, name);
                        newDataCollection.add(newData);
                    }
                    profile.set(s_id, newDataCollection);
                    profileDataStorage.edit(pname, profile);
                }
            }
        }
    }

}
