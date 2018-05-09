/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.core.data.storage.backend.json.script;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.storage.C;
import ryey.easer.core.data.storage.backend.Serializer;
import ryey.easer.core.data.storage.backend.UnableToSerializeException;
import ryey.easer.plugins.PluginRegistry;

class ScriptSerializer implements Serializer<ScriptStructure> {

    /**
     * {@inheritDoc}
     * This method assumes the scenario has already been serialized, so the name can uniquely identify a scenario
     */
    public String serialize(ScriptStructure script) throws UnableToSerializeException {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(C.NAME, script.getName());
            jsonObject.put(C.VERSION, C.VERSION_CURRENT);
            jsonObject.put(C.ACTIVE, script.isActive());
            jsonObject.put(C.PROFILE, script.getProfileName());
            jsonObject.put(C.AFTER, script.getParentName());

            if (script.getScenario() != null) {
                JSONObject trigger = serialize_scenario_trigger(script.getScenario());
                jsonObject.put(C.TRIG, trigger);

                if (!script.getScenario().isTmpScenario()) {
                    jsonObject.put(C.REVERSE, script.isReverse());
                    jsonObject.put(C.REPEATABLE, script.isRepeatable());
                    jsonObject.put(C.PERSISTENT, script.isPersistent());
                }
            } else { // if (script.getCondition() != null) {
                JSONObject trigger = serialize_condition_trigger(script.getCondition());
                jsonObject.put(C.TRIG, trigger);
                jsonObject.put(C.REVERSE, script.isReverse());
            }

            return jsonObject.toString();
        } catch (JSONException e) {
            throw new UnableToSerializeException(e.getMessage());
        }
    }

    JSONObject serialize_scenario_trigger(ScenarioStructure scenario) throws JSONException {
        if (scenario.isTmpScenario())
            return serialize_event(scenario.getEventData());
        else {
            JSONObject json_trigger = new JSONObject();
            json_trigger.put(C.TYPE, C.TriggerType.T_PRE);
            json_trigger.put(C.SCENARIO, scenario.getName());
            return json_trigger;
        }
    }

    JSONObject serialize_event(EventData event) throws JSONException {
        JSONObject json_trigger_raw = new JSONObject();
        json_trigger_raw.put(C.TYPE, C.TriggerType.T_RAW);
        json_trigger_raw.put(C.SIT, serialize_situation(event));
        return json_trigger_raw;
    }

    JSONObject serialize_situation(EventData event) throws JSONException {
        JSONObject json_situation = new JSONObject();
        json_situation.put(C.SPEC, PluginRegistry.getInstance().event().findPlugin(event).id());
        json_situation.put(C.DATA, event.serialize(C.Format.JSON));
        return json_situation;
    }

    private JSONObject serialize_condition_trigger(ConditionStructure condition) throws JSONException {
        JSONObject json_trigger = new JSONObject();
        json_trigger.put(C.TYPE, C.TriggerType.T_CONDITION);
        json_trigger.put(C.CONDITION, condition.getName());
        return json_trigger;
    }

}
