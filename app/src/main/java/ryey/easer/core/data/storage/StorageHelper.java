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
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ryey.easer.R;
import ryey.easer.core.data.Named;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.ScriptTree;
import ryey.easer.core.data.Verifiable;
import ryey.easer.core.data.WithCreatedVersion;

public class StorageHelper {

    public static boolean hasOldData(Context context) {
        AbstractDataStorage<?, ?> []dataStorages = {
                ProfileDataStorage.getInstance(context),
                ScriptDataStorage.getInstance(context),
                ScenarioDataStorage.getInstance(context),
                ConditionDataStorage.getInstance(context),
        };
        for (AbstractDataStorage<?, ?> dataStorage : dataStorages) {
            for (String name : dataStorage.list()) {
                if (dataStorage.get(name).createdVersion() < C.VERSION_CURRENT) {
                    return true;
                }
            }
        }
        File old_event_dir = new File(context.getFilesDir(), "event");
        if (old_event_dir.exists() && old_event_dir.isDirectory())
            return true;
        return false;
    }

    public static boolean convertToNewData(Context context) {
        Toast.makeText(context, R.string.message_convert_data_start, Toast.LENGTH_SHORT).show();

        File event_dir = new File(context.getFilesDir(), "event");
        File script_dir = new File(context.getFilesDir(), "script");
        if (event_dir.exists() && event_dir.isDirectory()) {
            if (!event_dir.renameTo(script_dir)) {
                Logger.e("Failed to rename \"event\" directory to \"script\".");
                Toast.makeText(context, R.string.message_convert_data_abort, Toast.LENGTH_LONG).show();
                return false;
            }
        }

        AbstractDataStorage<?, ?> []dataStorages = {
                ProfileDataStorage.getInstance(context),
                ScriptDataStorage.getInstance(context),
                ScenarioDataStorage.getInstance(context),
                ConditionDataStorage.getInstance(context),
        };
        String []tags = {
                "Profile",
                "Script",
                "Scenario",
                "Condition",
        };
        for (int i = 0; i < dataStorages.length; i++) {
            AbstractDataStorage<?, ?> dataStorage = dataStorages[i];
            String tag = tags[i];
            try {
                _convert(dataStorage);
            } catch (ConvertFailedException e) {
                Logger.e("Failed to convert <%s> <%s> to new format.", tag, e.name);
                Toast.makeText(context, R.string.message_convert_data_abort, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        Toast.makeText(context, R.string.message_convert_data_finish, Toast.LENGTH_LONG).show();
        return true;
    }

    private static <T extends Named & Verifiable & WithCreatedVersion> void _convert(AbstractDataStorage<T, ?> dataStorage) throws ConvertFailedException {
        for (String name : dataStorage.list()) {
            try {
                dataStorage.edit(name, dataStorage.get(name));
            } catch (IOException e) {
                throw new ConvertFailedException(e, name);
            }
        }
    }

    static boolean isSafeToDeleteProfile(Context context, String name) {
        ScriptDataStorage scriptDataStorage = ScriptDataStorage.getInstance(context);
        for (ScriptStructure scriptStructure : scriptDataStorage.allScripts()) {
            if (name.equals(scriptStructure.getProfileName()))
                return false;
        }
        return true;
    }

    static boolean isSafeToDeleteScenario(Context context, String name) {
        ScriptDataStorage scriptDataStorage = ScriptDataStorage.getInstance(context);
        for (ScriptStructure scriptStructure : scriptDataStorage.allScripts()) {
            if (name.equals(scriptStructure.getScenario().getName()))
                return false;
        }
        return true;
    }

    static List<ScriptTree> eventListToTrees(List<ScriptStructure> events) {
        List<ScriptTree> scriptTreeList = new ArrayList<>();
        Map<String, List<ScriptStructure>> eventIntermediateDataMap = StorageHelper.scriptParentMap(events);

        // construct the forest from the map
        // assume no loops
        List<ScriptStructure> int_roots = eventIntermediateDataMap.get(null);
        if (int_roots != null) {
            for (ScriptStructure int_root : int_roots) {
                ScriptTree tree = new ScriptTree(int_root);
                scriptTreeList.add(tree);
                StorageHelper.mapToTreeList(eventIntermediateDataMap, tree);
            }
        }

        return scriptTreeList;
    }

    static Map<String, List<ScriptStructure>> scriptParentMap(List<ScriptStructure> scripts) {
        Map<String, List<ScriptStructure>> scriptIntermediateDataMap = new HashMap<>();
        for (ScriptStructure script : scripts) {
            if (!scriptIntermediateDataMap.containsKey(script.getParentName())) {
                scriptIntermediateDataMap.put(script.getParentName(), new ArrayList<ScriptStructure>());
            }
            scriptIntermediateDataMap.get(script.getParentName()).add(script);
        }
        return scriptIntermediateDataMap;
    }

    static void mapToTreeList(Map<String, List<ScriptStructure>> eventIntermediateDataMap, ScriptTree node) {
        List<ScriptStructure> scriptStructureList = eventIntermediateDataMap.get(node.getName());
        if (scriptStructureList == null)
            return;
        for (ScriptStructure int_node : scriptStructureList) {
            ScriptTree sub_node = new ScriptTree(int_node);
            node.addSub(sub_node);
            mapToTreeList(eventIntermediateDataMap, sub_node);
        }
    }

    private static class ConvertFailedException extends IOException {
        private final String name;
        private ConvertFailedException(IOException e, String name) {
            super(e);
            this.name = name;
        }
    }
}
