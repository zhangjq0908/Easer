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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ryey.easer.R;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.EventTree;
import ryey.easer.core.data.Named;
import ryey.easer.core.data.Verifiable;
import ryey.easer.core.data.WithCreatedVersion;

public class StorageHelper {

    public static boolean hasOldData(Context context) {
        AbstractDataStorage<?, ?> []dataStorages = {
                ProfileDataStorage.getInstance(context),
                EventDataStorage.getInstance(context),
                ScenarioDataStorage.getInstance(context),
        };
        for (AbstractDataStorage<?, ?> dataStorage : dataStorages) {
            for (String name : dataStorage.list()) {
                if (dataStorage.get(name).createdVersion() < C.VERSION_CURRENT) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean convertToNewData(Context context) {
        Toast.makeText(context, R.string.message_convert_data_start, Toast.LENGTH_SHORT).show();
        AbstractDataStorage<?, ?> []dataStorages = {
                ProfileDataStorage.getInstance(context),
                EventDataStorage.getInstance(context),
                ScenarioDataStorage.getInstance(context),
        };
        String []tags = {
                "Profile",
                "Event",
                "Scenario",
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
        EventDataStorage eventDataStorage = EventDataStorage.getInstance(context);
        for (EventStructure eventStructure : eventDataStorage.allEvents()) {
            if (name.equals(eventStructure.getProfileName()))
                return false;
        }
        return true;
    }

    static boolean isSafeToDeleteEvent(Context context, String name) {
        EventDataStorage eventDataStorage = EventDataStorage.getInstance(context);
        for (EventStructure eventStructure : eventDataStorage.allEvents()) {
            if (name.equals(eventStructure.getParentName()))
                return false;
        }
        return true;
    }

    static boolean isSafeToDeleteScenario(Context context, String name) {
        EventDataStorage eventDataStorage = EventDataStorage.getInstance(context);
        for (EventStructure eventStructure : eventDataStorage.allEvents()) {
            if (name.equals(eventStructure.getScenario().getName()))
                return false;
        }
        return true;
    }

    static List<EventTree> eventListToTrees(List<EventStructure> events) {
        List<EventTree> eventTreeList = new ArrayList<>();
        Map<String, List<EventStructure>> eventIntermediateDataMap = StorageHelper.eventParentMap(events);

        // construct the forest from the map
        // assume no loops
        List<EventStructure> int_roots = eventIntermediateDataMap.get(null);
        if (int_roots != null) {
            for (EventStructure int_root : int_roots) {
                EventTree tree = new EventTree(int_root);
                eventTreeList.add(tree);
                StorageHelper.mapToTreeList(eventIntermediateDataMap, tree);
            }
        }

        return eventTreeList;
    }

    static Map<String, List<EventStructure>> eventParentMap(List<EventStructure> events) {
        Map<String, List<EventStructure>> eventIntermediateDataMap = new HashMap<>();
        for (EventStructure event : events) {
            if (!eventIntermediateDataMap.containsKey(event.getParentName())) {
                eventIntermediateDataMap.put(event.getParentName(), new ArrayList<EventStructure>());
            }
            eventIntermediateDataMap.get(event.getParentName()).add(event);
        }
        return eventIntermediateDataMap;
    }

    static void mapToTreeList(Map<String, List<EventStructure>> eventIntermediateDataMap, EventTree node) {
        List<EventStructure> eventStructureList = eventIntermediateDataMap.get(node.getName());
        if (eventStructureList == null)
            return;
        for (EventStructure int_node : eventStructureList) {
            EventTree sub_node = new EventTree(int_node);
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
