package ryey.easer.core.data.storage.backend.json.profile;

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
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.ProfileDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.NC;

public class JsonProfileDataStorageBackend implements ProfileDataStorageBackendInterface {

    private static JsonProfileDataStorageBackend instance = null;
    private static Context s_context = null;
    private static File dir;

    public static JsonProfileDataStorageBackend getInstance(Context context) {
        if (instance == null) {
            if (context != null)
                s_context = context;
            dir = IOUtils.mustGetSubDir(s_context.getFilesDir(), "profile");
            instance = new JsonProfileDataStorageBackend();
        }
        return instance;
    }

    private JsonProfileDataStorageBackend() {
    }

    @Override
    public boolean has(String name) {
        return IOUtils.fileExists(dir, name + NC.SUFFIX);
    }

    @Override
    public List<String> list() {
        ArrayList<String> list = new ArrayList<>();
        for (ProfileStructure profile : all()) {
            list.add(profile.getName());
        }
        return list;
    }

    @Override
    public ProfileStructure get(String name) throws IllegalStorageDataException {
        File file = new File(dir, name + NC.SUFFIX);
        return get(file);
    }

    private ProfileStructure get(File file) throws IllegalStorageDataException {
        ProfileParser parser = new ProfileParser();
        try {
            FileInputStream fin = new FileInputStream(file);
            ProfileStructure profileStructure = parser.parse(fin);
            fin.close();
            return profileStructure;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        throw new IllegalAccessError();
    }

    @Override
    public void add(ProfileStructure profile) throws IOException {
        File file = new File(dir, profile.getName() + NC.SUFFIX);
        try {
            FileOutputStream fout = new FileOutputStream(file);
            ProfileSerializer serializer = new ProfileSerializer();
            String serialized_str = serializer.serialize(profile);
            fout.write(serialized_str.getBytes());
            fout.close();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to serialize to JSON");
        }
    }

    @Override
    public void delete(String name) {
        File file = new File(dir, name + NC.SUFFIX);
        if (!file.delete())
            throw new IllegalStateException("Unable to delete " + file);
    }

    @Override
    public List<ProfileStructure> all() {
        List<ProfileStructure> list = new ArrayList<>();
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile()) {
                    if (pathname.getName().endsWith(NC.SUFFIX)) {
                        return true;
                    }
                }
                return false;
            }
        });
        for (File file : files) {
            try {
                list.add(get(file));
            } catch (IllegalStorageDataException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
