package ryey.easer.core.data.storage.json.event;

import android.content.Context;

import org.json.JSONException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.EventTree;
import ryey.easer.core.data.storage.EventDataStorageBackendInterface;
import ryey.easer.core.data.storage.FileUtils;
import ryey.easer.core.data.storage.StorageHelper;

public class JsonEventDataStorageBackend implements EventDataStorageBackendInterface {

    private static JsonEventDataStorageBackend instance = null;
    private static Context s_context = null;
    private static File dir;

    public static JsonEventDataStorageBackend getInstance(Context context) throws IOException {
        if (instance == null) {
            if (context != null)
                s_context = context;
            dir = FileUtils.getSubDir(s_context.getFilesDir(), "event");
            instance = new JsonEventDataStorageBackend();
        }
        return instance;
    }

    private JsonEventDataStorageBackend() {
    }

    @Override
    public List<String> list() {
        ArrayList<String> list = new ArrayList<>();
        for (EventStructure event : allEvents()) {
            list.add(event.getName());
        }
        return list;
    }


    @Override
    public EventStructure get(String name) {
        File file = new File(dir, name + ".json");
        return get(file);
    }

    private EventStructure get(File file) {
        try {
            FileInputStream fin = new FileInputStream(file);
            EventParser parser = new EventParser();
            return parser.parse(fin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalStorageDataException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(EventStructure event) {
        File file = new File(dir, event.getName() + ".json");
        try {
            FileOutputStream fout = new FileOutputStream(file);
            EventSerializer serializer = new EventSerializer();
            String serialized = serializer.serialize(event);
            fout.write(serialized.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String name) {
        if (!StorageHelper.isSafeToDeleteEvent(name))
            return false;
        File file = new File(dir, name + ".json");
        return file.delete();
    }

    @Override
    public boolean edit(String oldName, EventStructure event) {
        File old_file = new File(dir, oldName + ".json");
        boolean success = false;
        success = old_file.delete();
        success = success && add(event);
        if (success)
            StorageHelper.updateProfileNameRef(oldName, event.getName());
        return success;
    }

    @Override
    public List<EventTree> getEventTrees() {
        return StorageHelper.eventListToTrees(allEvents());
    }

    @Override
    public boolean handleProfileRename(String oldName, String newName) {
        for (EventStructure event : allEvents()) {
            if (oldName.equals(event.getProfileName())) {
                event.setProfileName(newName);
                edit(event.getName(), event);
            }
        }
        return true;
    }

    public List<EventStructure> allEvents() {
        List<EventStructure> list = new ArrayList<>();
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile()) {
                    if (pathname.getName().endsWith(".json")) {
                        return true;
                    }
                }
                return false;
            }
        });
        for (File file : files) {
            EventStructure event = get(file);
            if (event != null)
                list.add(event);
        }
        return list;
    }
}
