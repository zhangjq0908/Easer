package ryey.easer.core.data.storage.backend.json.scenario;

import android.content.Context;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.storage.backend.FileDataStorageBackendHelper;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.ScenarioDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.NC;

public class JsonScenarioDataStorageBackend implements ScenarioDataStorageBackendInterface {

    private static JsonScenarioDataStorageBackend instance = null;
    private static Context s_context = null;
    private static File dir;

    public static JsonScenarioDataStorageBackend getInstance(Context context) {
        if (instance == null) {
            if (context != null)
                s_context = context;
            dir = IOUtils.mustGetSubDir(s_context.getFilesDir(), "scenario");
            instance = new JsonScenarioDataStorageBackend();
        }
        return instance;
    }

    @Override
    public boolean has(String name) {
        return IOUtils.fileExists(dir, name + NC.SUFFIX);
    }

    @Override
    public List<String> list() {
        ArrayList<String> list = new ArrayList<>();
        for (ScenarioStructure scenario : all()) {
            list.add(scenario.getName());
        }
        return list;
    }

    @Override
    public ScenarioStructure get(String name) throws FileNotFoundException, IllegalStorageDataException {
        File file = new File(dir, name + NC.SUFFIX);
        return get(file);
    }

    private ScenarioStructure get(File file) throws FileNotFoundException, IllegalStorageDataException {
        ScenarioParser parser = new ScenarioParser();
        return FileDataStorageBackendHelper.get(parser, file);
    }

    @Override
    public void write(ScenarioStructure profile) throws IOException {
        File file = new File(dir, profile.getName() + NC.SUFFIX);
        ScenarioSerializer serializer = new ScenarioSerializer();
        FileDataStorageBackendHelper.write(serializer, file, profile);
    }

    @Override
    public void delete(String name) {
        File file = new File(dir, name + NC.SUFFIX);
        if (!file.delete())
            throw new IllegalStateException("Unable to delete " + file);
    }

    @Override
    public List<ScenarioStructure> all() {
        List<ScenarioStructure> list = new ArrayList<>();
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
            } catch (FileNotFoundException e) {
                throw new IllegalStateException(e.getCause());
            }
        }
        return list;
    }
}
