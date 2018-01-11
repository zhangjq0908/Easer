package ryey.easer.core.data.storage.backend.json.scenario;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.storage.C;
import ryey.easer.core.data.storage.backend.Serializer;
import ryey.easer.core.data.storage.backend.UnableToSerializeException;
import ryey.easer.plugins.PluginRegistry;

public class ScenarioSerializer implements Serializer<ScenarioStructure> {
    @Override
    public String serialize(ScenarioStructure data) throws UnableToSerializeException {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(C.NAME, data.getName());
            jsonObject.put(C.VERSION, C.VERSION_CURRENT);

            EventData event = data.getEventData();
            jsonObject.put(C.LOGIC, event.type().toString());
            jsonObject.put(C.SIT, serialize_situation(event));
            return jsonObject.toString();
        } catch (JSONException e) {
            throw new UnableToSerializeException(e.getMessage());
        }
    }

    private static JSONObject serialize_situation(EventData event) throws JSONException {
        JSONObject json_situation = new JSONObject();
        json_situation.put(C.SPEC, PluginRegistry.getInstance().event().findPlugin(event).id());
        json_situation.put(C.DATA, event.serialize(C.Format.JSON));
        return json_situation;
    }
}
