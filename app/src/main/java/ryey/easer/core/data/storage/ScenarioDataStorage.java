package ryey.easer.core.data.storage;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.storage.backend.ScenarioDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.scenario.JsonScenarioDataStorageBackend;

public class ScenarioDataStorage extends AbstractDataStorage<ScenarioStructure, ScenarioDataStorageBackendInterface> {


    private static ScenarioDataStorage instance = null;

    Context context;
    private static Map<String, ScenarioStructure> tmpScenarios = new ArrayMap<>();

    public static ScenarioDataStorage getInstance(Context context) {
        if (instance == null) {
            instance = new ScenarioDataStorage();
            instance.storage_backend_list = new ScenarioDataStorageBackendInterface[] {
                    JsonScenarioDataStorageBackend.getInstance(context),
            };
            instance.context = context;
        }
        return instance;
    }

    @Override
    public ScenarioStructure get(String name) {
        if (tmpScenarios.containsKey(name))
            return tmpScenarios.get(name);
        return super.get(name);
    }

    @Override
    boolean isSafeToDelete(String name) {
        return true;
    }

    @Override
    public boolean delete(String name) {
        return super.delete(name);
    }

    public static void recordTmp(ScenarioStructure scenario) {
        tmpScenarios.put(scenario.getName(), scenario);
    }

    public static boolean hasTmp(String name) {
        return tmpScenarios.containsKey(name);
    }

    public static void removeTmp(String name) {
        tmpScenarios.remove(name);
    }

    public static String tmpScenarioName(String eventName) {
        return  "tmpScenario___" + eventName;
    }
}
