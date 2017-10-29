package ryey.easer.core.data.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.EventTree;

public class StorageHelper {
    public static boolean isSafeToDeleteProfile(String name) {
        return true;
    }

    public static boolean isSafeToDeleteEvent(String name) {
        return true;
    }

    public static void updateProfileNameRef(String oldName, String newName) {

    }

    public static void updateEventNameRef(String oldName, String newName) {

    }

    public static List<EventTree> eventListToTrees(List<EventStructure> events) {
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

    public static Map<String, List<EventStructure>> eventParentMap(List<EventStructure> events) {
        Map<String, List<EventStructure>> eventIntermediateDataMap = new HashMap<>();
        for (EventStructure event : events) {
            if (!eventIntermediateDataMap.containsKey(event.getParentName())) {
                eventIntermediateDataMap.put(event.getParentName(), new ArrayList<EventStructure>());
            }
            eventIntermediateDataMap.get(event.getParentName()).add(event);
        }
        return eventIntermediateDataMap;
    }

    public static void mapToTreeList(Map<String, List<EventStructure>> eventIntermediateDataMap, EventTree node) {
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
