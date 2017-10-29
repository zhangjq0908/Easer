package ryey.easer.core.data.storage;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.EventTree;
import ryey.easer.core.data.storage.json.event.JsonEventDataStorageBackend;
import ryey.easer.core.data.storage.xml.event.XmlEventDataStorageBackend;

public class EventDataStorage {

    private static EventDataStorage instance = null;

    JsonEventDataStorageBackend json_storage;
    XmlEventDataStorageBackend xml_storage;

    public static EventDataStorage getInstance(Context context) throws IOException {
        if (instance == null) {
            instance = new EventDataStorage();
            instance.json_storage = JsonEventDataStorageBackend.getInstance(context);
            instance.xml_storage = XmlEventDataStorageBackend.getInstance(context);
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
        if (json_storage.list().contains(name))
            return json_storage.delete(name);
        else if (xml_storage.list().contains(name))
            return xml_storage.delete(name);
        return false;
    }

    public boolean edit(String oldName, EventStructure event) {
        if (!add(event))
            return false;
        if (json_storage.list().contains(oldName))
            return json_storage.delete(oldName);
        else if (xml_storage.list().contains(oldName))
            return xml_storage.delete(oldName);
        return false;
    }

    public List<EventTree> getEventTrees() {
        List<EventStructure> list = json_storage.allEvents();
        list.addAll(xml_storage.allEvents());
        return StorageHelper.eventListToTrees(list);
    }

    public boolean handleProfileRename(String oldName, String newName) {
        boolean success;
        success = json_storage.handleProfileRename(oldName, newName);
        return xml_storage.handleProfileRename(oldName, newName) && success;
    }
}
