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
import android.support.annotation.NonNull;
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
import ryey.easer.core.data.storage.backend.DataStorageBackendCommonInterface;

public class StorageHelper {

    public static boolean hasDeprecatedFormattedData(@NonNull Context context) {
        EventDataStorage eventDataStorage = EventDataStorage.getInstance(context);
        if (eventDataStorage.storage_backend_list[1].list().size() > 0)
            return true;
        ProfileDataStorage profileDataStorage = ProfileDataStorage.getInstance(context);
        if (profileDataStorage.storage_backend_list[1].list().size() > 0)
            return true;
        return false;
    }

    public static boolean convertToNewFormat(Context context) {
        Toast.makeText(context, R.string.message_convert_data_start, Toast.LENGTH_SHORT).show();
        ProfileDataStorage profileDataStorage = ProfileDataStorage.getInstance(context);
        try {
            convert(profileDataStorage.storage_backend_list);
        } catch (ConvertFailedException e) {
            Logger.e("Failed to convert Profile <%s> to new format.", e.name);
            Toast.makeText(context, R.string.message_convert_data_abort, Toast.LENGTH_LONG).show();
            return false;
        }
        EventDataStorage eventDataStorage = EventDataStorage.getInstance(context);
        try {
            convert(eventDataStorage.storage_backend_list);
        } catch (ConvertFailedException e) {
            Logger.e("Failed to convert Event <%s> to new format.", e.name);
            Toast.makeText(context, R.string.message_convert_data_abort, Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(context, R.string.message_convert_data_finish, Toast.LENGTH_LONG).show();
        return true;
    }

    private static <T extends Named> void convert(DataStorageBackendCommonInterface<T>[] backends) throws ConvertFailedException {
        DataStorageBackendCommonInterface<T> prefered = backends[0];
        for (int i = 1; i < backends.length; i++) {
            DataStorageBackendCommonInterface<T> backend = backends[i];
            for (T data : backend.all()) {
                try {
                    prefered.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ConvertFailedException(e, data.getName());
                }
                backend.delete(data.getName());
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
