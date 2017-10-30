package ryey.easer.core.data.storage;

import android.content.Context;

import java.util.List;

import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.backend.json.profile.JsonProfileDataStorageBackend;
import ryey.easer.core.data.storage.backend.xml.profile.XmlProfileDataStorageBackend;

public class ProfileDataStorage {

    private static ProfileDataStorage instance = null;

    Context context;
    JsonProfileDataStorageBackend json_storage;
    XmlProfileDataStorageBackend xml_storage;

    public static ProfileDataStorage getInstance(Context context) {
        if (instance == null) {
            instance = new ProfileDataStorage();
            instance.json_storage = JsonProfileDataStorageBackend.getInstance(context);
            instance.xml_storage = XmlProfileDataStorageBackend.getInstance(context);
            instance.context = context;
        }
        return instance;
    }

    public List<String> list() {
        List<String> list = json_storage.list();
        list.addAll(xml_storage.list());
        return list;
    }

    public ProfileStructure get(String name) {
        ProfileStructure event = json_storage.get(name);
        if (event == null)
            event = xml_storage.get(name);
        return event;
    }

    public boolean add(ProfileStructure profile) {
        if (json_storage.list().contains(profile.getName()) || xml_storage.list().contains(profile.getName()))
            return false;
        return json_storage.add(profile);
    }

    public boolean delete(String name) {
        if (!StorageHelper.isSafeToDeleteProfile(context, name))
            return false;
        if (json_storage.list().contains(name))
            return json_storage.delete(name);
        else if (xml_storage.list().contains(name))
            return xml_storage.delete(name);
        return false;
    }

    public boolean edit(String oldName, ProfileStructure profile) {
        if (oldName.equals(profile.getName()))
            return update(profile);
        if (!add(profile))
            return false;
        boolean succees;
        if (json_storage.list().contains(oldName))
            succees = json_storage.delete(oldName);
        else if (xml_storage.list().contains(oldName))
            succees = xml_storage.delete(oldName);
        else {
            throw new IllegalAccessError();
        }
        if (succees && oldName.equals(profile.getName())) {
            EventDataStorage eventDataStorage = EventDataStorage.getInstance(context);
            succees = eventDataStorage.handleProfileRename(oldName, profile.getName());
        }
        return succees;
    }

    boolean update(ProfileStructure profileStructure) {
        String name = profileStructure.getName();
        boolean success;
        if (json_storage.list().contains(name))
            success = json_storage.delete(name);
        else if (xml_storage.list().contains(name))
            success = xml_storage.delete(name);
        else
            throw new IllegalAccessError();
        if (success)
            return add(profileStructure);
        return false;
    }
}
