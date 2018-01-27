package ryey.easer.core.data.storage.backend.json.scenario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.storage.C;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.Parser;
import ryey.easer.plugins.PluginRegistry;

public class ScenarioParser implements Parser<ScenarioStructure> {

    @Override
    public ScenarioStructure parse(InputStream in) throws IOException, IllegalStorageDataException {
        try {
            JSONObject jsonObject = new JSONObject(IOUtils.inputStreamToString(in));
            int version = jsonObject.optInt(C.VERSION, C.VERSION_USE_SCENARIO);
            final String name = jsonObject.getString(C.NAME);
            EventType logic = EventType.valueOf(jsonObject.getString(C.LOGIC));
            JSONObject jsonObject_situation = jsonObject.getJSONObject(C.SIT);
            String spec = jsonObject_situation.getString(C.SPEC);
            EventData eventData = PluginRegistry.getInstance().event().findPlugin(spec)
                    .dataFactory()
                    .parse(jsonObject_situation.getString(C.DATA), C.Format.JSON, version);
            eventData.setType(logic);
            return new ScenarioStructure(name, eventData);
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e.getMessage());
        }
    }
}
