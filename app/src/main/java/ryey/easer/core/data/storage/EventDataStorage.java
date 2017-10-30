package ryey.easer.core.data.storage;

import android.content.Context;

import java.util.List;

import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.EventTree;
import ryey.easer.core.data.storage.backend.EventDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.event.JsonEventDataStorageBackend;
import ryey.easer.core.data.storage.backend.xml.event.XmlEventDataStorageBackend;

public class EventDataStorage {

    private static EventDataStorage instance = null;

    private Context context;
    private EventDataStorageBackendInterface json_storage;
    private EventDataStorageBackendInterface xml_storage;

    public static EventDataStorage getInstance(Context context) {
        if (instance == null) {
            instance = new EventDataStorage();
            instance.json_storage = JsonEventDataStorageBackend.getInstance(context);
            instance.xml_storage = XmlEventDataStorageBackend.getInstance(context);
            instance.context = context;
        }
        return instance;
    }

    private EventDataStorage() {}

    public List<String> list() {
        List<String> all = json_storage.list();
        all.addAll(xml_storage.list());
        return all;
    }

    public EventStructure get(String name) {
        EventStructure event = json_storage.get(name);
        if (event == null)
            event = xml_storage.get(name);
        return event;
    }

    public boolean add(EventStructure event) {
        if (json_storage.list().contains(event.getName()) || xml_storage.list().contains(event.getName()))
            return false;
        return json_storage.add(event);
    }

    public boolean delete(String name) {
        if (!StorageHelper.isSafeToDeleteEvent(context, name))
            return false;
        if (json_storage.list().contains(name))
            return json_storage.delete(name);
        else if (xml_storage.list().contains(name))
            return xml_storage.delete(name);
        return false;
    }

    public boolean edit(String oldName, EventStructure event) {
        if (oldName.equals(event.getName()))
            return update(event);
        if (!add(event))
            return false;
        boolean success;
        if (json_storage.list().contains(oldName))
            success = json_storage.delete(oldName);
        else if (xml_storage.list().contains(oldName))
            success = xml_storage.delete(oldName);
        else {
            throw new IllegalAccessError();
        }
        if (success) {
            if (!oldName.equals(event.getName())) {
                success = handleEventRename(oldName, event.getName());
            }
        }
        return success;
    }

    boolean update(EventStructure eventStructure) {
        String name = eventStructure.getName();
        boolean success;
        if (json_storage.list().contains(name))
            success = json_storage.delete(name);
        else if (xml_storage.list().contains(name))
            success = xml_storage.delete(name);
        else {
            throw new IllegalAccessError();
        }
        if (success)
            return add(eventStructure);
        return false;
    }

    public List<EventTree> getEventTrees() {
        return StorageHelper.eventListToTrees(allEvents());
    }

    List<EventStructure> allEvents() {
        List<EventStructure> list = json_storage.allEvents();
        list.addAll(xml_storage.allEvents());
        return list;
    }

    boolean handleProfileRename(String oldName, String newName) {
        for (EventStructure event : json_storage.allEvents()) {
            if (oldName.equals(event.getProfileName())) {
                event.setProfileName(newName);
                json_storage.update(event);
            }
        }

        for (EventStructure event : xml_storage.allEvents()) {
            if (oldName.equals(event.getProfileName())) {
                event.setProfileName(newName);
                xml_storage.update(event);
            }
        }

        return true;
    }

    boolean handleEventRename(String oldName, String newName) {
        boolean success = true;
        // alter subnodes to point to the new name
        List<EventStructure> subs = StorageHelper.eventParentMap(allEvents()).get(oldName);
        if (subs != null) {
            for (EventStructure sub : subs) {
                sub.setParentName(newName);
                if (!update(sub))
                    success = false;
            }

        }
        return success;
    }
}
