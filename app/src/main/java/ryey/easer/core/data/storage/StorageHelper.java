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
import ryey.easer.core.data.Verifiable;
import ryey.easer.core.data.WithCreatedVersion;

public class StorageHelper {

    private static boolean dirWithContent(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            if (dir.list().length > 0)
                return true;
        }
        return false;
    }

    public static boolean hasOldData(Context context) {
        AbstractDataStorage<?, ?> []dataStorages = {
                new ProfileDataStorage(context),
                new ScriptDataStorage(context),
                new EventDataStorage(context),
                new ConditionDataStorage(context),
        };
        for (AbstractDataStorage<?, ?> dataStorage : dataStorages) {
            for (String name : dataStorage.list()) {
                if (dataStorage.get(name).createdVersion() < C.VERSION_CURRENT) {
                    return true;
                }
            }
        }
        File dir_event = new File(context.getFilesDir(), "event");;
        File dir_scenario = new File(context.getFilesDir(), "scenario");
        File dir_script = new File(context.getFilesDir(), "script");
        if (dir_event.exists() && dir_scenario.exists())
            return true;
        if (dir_scenario.exists() && dir_script.exists())
            return true;
        return false;
    }

    public static boolean convertToNewData(Context context) {
        Toast.makeText(context, R.string.message_convert_data_start, Toast.LENGTH_SHORT).show();

        File dir_event = new File(context.getFilesDir(), "event");
        File dir_scenario = new File(context.getFilesDir(), "scenario");
        File dir_script = new File(context.getFilesDir(), "script");

        if (dirWithContent(dir_event) && dirWithContent(dir_scenario)) {
            if (!dir_event.renameTo(dir_script)) {
                Logger.e("Failed to rename \"event\" directory to \"script\".");
                Toast.makeText(context, R.string.message_convert_data_abort, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (dirWithContent(dir_scenario) && dirWithContent(dir_script)) {
            if (dir_event.exists() && ! dirWithContent(dir_event))
                if (!dir_event.delete()) {
                    Logger.e("Failed to delete empty directory \"event\".");
                    Toast.makeText(context, R.string.message_convert_data_abort, Toast.LENGTH_LONG).show();
                    return false;
                }
            if (!dir_scenario.renameTo(dir_event)) {
                Logger.e("Failed to rename \"scenario\" directory to \"event\".");
                Toast.makeText(context, R.string.message_convert_data_abort, Toast.LENGTH_LONG).show();
                return false;
            }
        }

        AbstractDataStorage<?, ?> []dataStorages = {
                new ProfileDataStorage(context),
                new ScriptDataStorage(context),
                new EventDataStorage(context),
                new ConditionDataStorage(context),
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

    private static class ConvertFailedException extends IOException {
        private final String name;
        private ConvertFailedException(IOException e, String name) {
            super(e);
            this.name = name;
        }
    }
}
