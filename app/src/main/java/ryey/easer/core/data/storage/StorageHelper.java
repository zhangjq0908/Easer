package ryey.easer.core.data.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.EventTree;

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
}
