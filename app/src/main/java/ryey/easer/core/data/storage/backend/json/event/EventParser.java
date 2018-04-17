package ryey.easer.core.data.storage.backend.json.event;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import ryey.easer.BuildConfig;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.storage.C;
import ryey.easer.core.data.storage.ScenarioDataStorage;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.Parser;
import ryey.easer.plugins.PluginRegistry;

class EventParser implements Parser<EventStructure> {

    final Context context;

    private EventStructure eventStructure;

    EventParser(Context context) {
        this.context = context;
    }

    public EventStructure parse(InputStream in) throws IOException, IllegalStorageDataException {
        try {
            JSONObject jsonObject = new JSONObject(IOUtils.inputStreamToString(in));
            int version = jsonObject.optInt(C.VERSION, C.VERSION_ADD_JSON);
            eventStructure = new EventStructure(version);
            eventStructure.setName(jsonObject.getString(C.NAME));
            eventStructure.setActive(jsonObject.optBoolean(C.ACTIVE, true));
            eventStructure.setProfileName(jsonObject.optString(C.PROFILE, null));
            eventStructure.setParentName(jsonObject.optString(C.AFTER, null));
            if (version < C.VERSION_USE_SCENARIO) { // Can be removed (because this is covered by the else statement)
                EventData eventData = parse_eventData(jsonObject.getJSONObject(C.TRIG), version);
                eventStructure.setEventData(eventData);
            } else {
                parseAndSet_trigger(jsonObject.getJSONObject(C.TRIG), version);
                if (!eventStructure.getScenario().isTmpScenario()) {
                    eventStructure.setReverse(jsonObject.getBoolean(C.REVERSE));
                    eventStructure.setRepeatable(jsonObject.getBoolean(C.REPEATABLE));
                    eventStructure.setPersistent(jsonObject.getBoolean(C.PERSISTENT));
                }
            }
            return eventStructure;
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e);
        }
    }

    private void parseAndSet_trigger(JSONObject jsonObject_trigger, int version) throws IllegalStorageDataException {
        try {
            String trigger_type = jsonObject_trigger.getString(C.TYPE);
            switch (trigger_type) {
                case C.TriggerType.T_RAW:
                    EventData eventData = parse_eventData(jsonObject_trigger, version);
                    eventStructure.setEventData(eventData);
                    break;
                case C.TriggerType.T_PRE:
                    String scenario_name = jsonObject_trigger.getString(C.SCENARIO);
                    ScenarioStructure scenario = ScenarioDataStorage.getInstance(context).get(scenario_name);
                    eventStructure.setScenario(scenario);
                    break;
                default:
                    throw new IllegalStorageDataException("Unexpected trigger type");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalStorageDataException(e);
        }
    }

    EventData parse_eventData(JSONObject jsonObject_trigger, int version) throws IllegalStorageDataException {
        try {
            String type = jsonObject_trigger.getString(C.TYPE);
            if (BuildConfig.DEBUG && !type.equals(C.TriggerType.T_RAW))
                throw new AssertionError();
            JSONObject jsonObject_situation = jsonObject_trigger.getJSONObject(C.SIT);
            String spec = jsonObject_situation.getString(C.SPEC);
            return PluginRegistry.getInstance().event().findPlugin(spec)
                    .dataFactory()
                    .parse(jsonObject_situation.getString(C.DATA), C.Format.JSON, version);
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e);
        }
    }
}
