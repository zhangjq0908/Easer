package ryey.easer.core.data.storage.backend.json.event;

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
import ryey.easer.core.data.storage.backend.EventDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.IOUtils;

public class JsonEventDataStorageBackend implements EventDataStorageBackendInterface {

    private static JsonEventDataStorageBackend instance = null;
    private static Context s_context = null;
    private static File dir;

    public static JsonEventDataStorageBackend getInstance(Context context) {
        if (instance == null) {
            if (context != null)
                s_context = context;
            dir = IOUtils.mustGetSubDir(s_context.getFilesDir(), "event");
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
        File file = new File(dir, name + ".json");
        return file.delete();
    }

    @Override
    public boolean update(EventStructure event) {
        return delete(event.getName()) && add(event);
    }

    @Override
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
