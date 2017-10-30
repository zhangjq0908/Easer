package ryey.easer.core.data.storage.backend.json.event;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.C;
import ryey.easer.core.data.storage.backend.Serializer;
import ryey.easer.core.data.storage.backend.UnableToSerializeException;
import ryey.easer.plugins.PluginRegistry;

class EventSerializer implements Serializer<EventStructure> {

    public String serialize(EventStructure event) throws UnableToSerializeException {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(C.NAME, event.getName());
            jsonObject.put(C.VERSION, C.VERSION_CURRENT);
            jsonObject.put(C.ACTIVE, event.isActive());
            jsonObject.put(C.PROFILE, event.getProfileName());
            jsonObject.put(C.AFTER, event.getParentName());

            jsonObject.put(C.TRIG, serialize_event(event.getEventData()));
            return jsonObject.toString();
        } catch (JSONException e) {
            throw new UnableToSerializeException(e.getMessage());
        }
    }

    JSONObject serialize_event(EventData event) throws JSONException {
        JSONObject json_trigger = new JSONObject();
        json_trigger.put(C.TYPE, C.TriggerType.T_RAW);
        json_trigger.put(C.LOGIC, event.type().toString());
        json_trigger.put(C.SIT, serialize_situation(event));
        return json_trigger;
    }

    JSONObject serialize_situation(EventData event) throws JSONException {
        JSONObject json_situation = new JSONObject();
        json_situation.put(C.SPEC, PluginRegistry.getInstance().event().findPlugin(event).name());
        json_situation.put(C.DATA, event.serialize(C.Format.JSON));
        return json_situation;
    }
}
