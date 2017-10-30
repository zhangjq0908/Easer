package ryey.easer.core.data.storage.backend.json.event;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import ryey.easer.BuildConfig;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.C;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.Parser;
import ryey.easer.plugins.PluginRegistry;

class EventParser implements Parser<EventStructure> {

    public EventStructure parse(InputStream in) throws IOException, IllegalStorageDataException {
        EventStructure eventStructure = new EventStructure();
        try {
            JSONObject jsonObject = new JSONObject(IOUtils.inputStreamToString(in));
            int version = jsonObject.optInt(C.VERSION_NAME, C.VERSION_CURRENT);
            eventStructure.setName(jsonObject.getString(C.NAME));
            eventStructure.setActive(jsonObject.optBoolean(C.ACTIVE, true));
            eventStructure.setProfileName(jsonObject.optString(C.PROFILE, null));
            eventStructure.setParentName(jsonObject.optString(C.AFTER, null));
            eventStructure.setEventData(parse_eventData(jsonObject.getJSONObject(C.TRIG), version));
            return eventStructure;
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e.getMessage());
        }
    }

    EventData parse_eventData(JSONObject jsonObject_trigger, int version) throws IllegalStorageDataException {
        try {
            String type = jsonObject_trigger.getString(C.TYPE);
            if (BuildConfig.DEBUG && !type.equals(C.TriggerType.T_RAW))
                throw new AssertionError();
            EventType logic = EventType.valueOf(jsonObject_trigger.getString(C.LOGIC));
            JSONObject jsonObject_situation = jsonObject_trigger.getJSONObject(C.SIT);
            String spec = jsonObject_situation.getString(C.SPEC);
            EventData eventData = PluginRegistry.getInstance().event().findPlugin(spec).data();
            eventData.setType(logic);
            eventData.parse(jsonObject_situation.getString(C.DATA), C.Format.JSON, version);
            return eventData;
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e.getMessage());
        }
    }
}
