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
    public List<String> list() {
        ArrayList<String> list = new ArrayList<>();
        for (ProfileStructure profile : allProfiles()) {
            list.add(profile.getName());
        }
        return list;
    }

    @Override
    public ProfileStructure get(String name) {
        File file = new File(dir, name + ".json");
        try {
            FileInputStream fin = new FileInputStream(file);
            ProfileParser parser = new ProfileParser();
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
    public boolean add(ProfileStructure profile) {
        File file = new File(dir, profile.getName() + ".json");
        try {
            FileOutputStream fout = new FileOutputStream(file);
            ProfileSerializer serializer = new ProfileSerializer();
            String serialized_str = serializer.serialize(profile);
            fout.write(serialized_str.getBytes());
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
    public List<ProfileStructure> allProfiles() {
        List<ProfileStructure> list = new ArrayList<>();
        try {
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
            ProfileParser parser = new ProfileParser();
            for (File file : files) {
                try {
                    FileInputStream fin = new FileInputStream(file);
                    ProfileStructure profile = parser.parse(fin);
                    list.add(profile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalStorageDataException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
