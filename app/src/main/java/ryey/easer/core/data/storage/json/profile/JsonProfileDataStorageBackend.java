package ryey.easer.core.data.storage.json.profile;

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
import ryey.easer.core.data.storage.FileUtils;
import ryey.easer.core.data.storage.ProfileDataStorageBackendInterface;
import ryey.easer.core.data.storage.StorageHelper;

public class JsonProfileDataStorageBackend implements ProfileDataStorageBackendInterface {

    private static JsonProfileDataStorageBackend instance = null;
    private static Context s_context = null;
    private static File dir;

    public static JsonProfileDataStorageBackend getInstance(Context context) throws IOException {
        if (instance == null) {
            if (context != null)
                s_context = context;
            dir = FileUtils.getSubDir(s_context.getFilesDir(), "profile");
            instance = new JsonProfileDataStorageBackend();
        }
        return instance;
    }

    private JsonProfileDataStorageBackend() {
    }

    @Override
    public List<String> list() {
        ArrayList<String> list = new ArrayList<>();
        for (ProfileStructure profile : allJsonProfiles()) {
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
        if (!StorageHelper.isSafeToDeleteProfile(name))
            return false;
        File file = new File(dir, name + ".json");
        return file.delete();
    }

    @Override
    public boolean edit(String oldName, ProfileStructure profile) {
        File old_file = new File(dir, oldName + ".json");
        boolean success = false;
        success = old_file.delete();
        success = success && add(profile);
        if (success)
            StorageHelper.updateProfileNameRef(oldName, profile.getName());
        return success;
    }

    public List<ProfileStructure> allJsonProfiles() {
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
