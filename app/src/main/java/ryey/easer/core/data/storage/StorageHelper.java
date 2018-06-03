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
import java.util.LinkedList;
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

    static List<ScriptTree> eventListToTrees(List<ScriptStructure> events) {
        Map<String, List<ScriptStructure>> eventIntermediateDataMap = scriptParentMap(events);
        // construct the forest from the map
        // assume no loops
        return mapToTreeList(eventIntermediateDataMap, null);
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

    private static List<ScriptTree> mapToTreeList(Map<String, List<ScriptStructure>> eventIntermediateDataMap, String name) {
        List<ScriptTree> treeList = new LinkedList<>();
        List<ScriptStructure> scriptStructureList = eventIntermediateDataMap.get(name);
        if (scriptStructureList != null) {
            for (ScriptStructure int_node : scriptStructureList) {
                if (int_node.isValid()) { //TODO: Move this check to EHService and/or Lotus
                    treeList.add(new ScriptTree(
                            int_node,
                            mapToTreeList(eventIntermediateDataMap, int_node.getName())
                    ));
                }
            }
        }
        return treeList;
    }

    private static class ConvertFailedException extends IOException {
        private final String name;
        private ConvertFailedException(IOException e, String name) {
            super(e);
            this.name = name;
        }
    }
}
