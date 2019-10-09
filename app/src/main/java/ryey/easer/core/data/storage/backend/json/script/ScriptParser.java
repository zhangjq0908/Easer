/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import ryey.easer.BuildConfig;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.DynamicsLink;
import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.core.data.BuilderInfoClashedException;
import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.storage.C;
import ryey.easer.core.data.storage.ConditionDataStorage;
import ryey.easer.core.data.storage.EventDataStorage;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.Parser;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.LocalSkillRegistry;

class ScriptParser implements Parser<ScriptStructure> {

    final Context context;

    private ScriptStructure.Builder builder;

    ScriptParser(Context context) {
        this.context = context;
    }

    public ScriptStructure parse(InputStream in) throws IOException, IllegalStorageDataException {
        try {
            JSONObject jsonObject = new JSONObject(IOUtils.inputStreamToString(in));
            int version = jsonObject.optInt(C.VERSION, C.VERSION_ADD_JSON);
            builder = new ScriptStructure.Builder(version);
            builder.setName(jsonObject.getString(C.NAME))
                    .setActive(jsonObject.optBoolean(C.ACTIVE, true))
                    .setProfileName(jsonObject.optString(C.PROFILE, null));
            {
                if (version < C.VERSION_GRAPH_SCRIPT) {
                    if (jsonObject.has(C.AFTER)) {
                        String parent = jsonObject.getString(C.AFTER);
                        builder.addPredecessor(parent);
                    }
                } else {
                    JSONArray arrayPredecessors = jsonObject.optJSONArray(C.AFTER);
                    if (arrayPredecessors != null) {
                        for (int i = 0; i < arrayPredecessors.length(); i++) {
                            builder.addPredecessor(arrayPredecessors.getString(i));
                        }
                    }
                }
            }
            if (version < C.VERSION_USE_SCENARIO) { // Can be removed (because this is covered by the else statement)
                EventData eventData = parse_eventData(jsonObject.getJSONObject(C.TRIG), version);
                builder.setTmpEvent(eventData);
            } else {
                parseAndSet_trigger(jsonObject.getJSONObject(C.TRIG), version);
                if (jsonObject.has(C.REVERSE))
                    builder.setReverse(jsonObject.getBoolean(C.REVERSE));
                try {
                    if (builder.isEvent() && !builder.isTmpEvent()) {
                        builder.setReverse(jsonObject.getBoolean(C.REVERSE));
                        builder.setRepeatable(jsonObject.getBoolean(C.REPEATABLE));
                        builder.setPersistent(jsonObject.getBoolean(C.PERSISTENT));
                    } else {
                        builder.setReverse(jsonObject.getBoolean(C.REVERSE));
                    }
                } catch (BuilderInfoClashedException e) {
                    throw new IllegalStateException(e);
                }
            }

            //dynamics
            if (version >= C.VERSION_ADD_DYNAMICS) {
                JSONObject json_dynamics = jsonObject.optJSONObject(C.DYNAMICS);
                if (json_dynamics != null) {
                    DynamicsLink dynamicsLink = new DynamicsLink();
                    for (Iterator<String> it = json_dynamics.keys(); it.hasNext(); ) {
                        String placeholder = it.next();
                        String property = json_dynamics.getString(placeholder);
                        dynamicsLink.put(placeholder, property);
                    }
                    builder.setDynamicsLink(dynamicsLink);
                }
            }

            return builder.build();
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e);
        } catch (BuilderInfoClashedException e) {
            throw new IllegalStorageDataException(e);
        }
    }

    private void parseAndSet_trigger(JSONObject jsonObject_trigger, int version) throws IllegalStorageDataException {
        try {
            String trigger_type = jsonObject_trigger.getString(C.TYPE);
            switch (trigger_type) {
                case C.TriggerType.T_RAW:
                    EventData eventData = parse_eventData(jsonObject_trigger, version);
                    builder.setTmpEvent(eventData);
                    break;
                case C.TriggerType.T_PRE:
                    String event_name;
                    if (version < C.VERSION_RENAME_SCENARIO_TO_EVENT)
                        event_name = jsonObject_trigger.getString(C.SCENARIO);
                    else
                        event_name = jsonObject_trigger.getString(C.EVENT);
                    EventStructure event = new EventDataStorage(context).get(event_name);
                    builder.setEvent(event);
                    break;
                case C.TriggerType.T_CONDITION:
                    String condition_name = jsonObject_trigger.getString(C.CONDITION);
                    ConditionStructure condition = new ConditionDataStorage(context).get(condition_name);
                    builder.setCondition(condition);
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
            return LocalSkillRegistry.getInstance().event().findSkill(spec)
                    .dataFactory()
                    .parse(jsonObject_situation.getString(C.DATA), PluginDataFormat.JSON, version);
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e);
        }
    }
}
