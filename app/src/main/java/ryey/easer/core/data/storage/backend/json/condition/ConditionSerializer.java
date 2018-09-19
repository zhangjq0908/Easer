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

package ryey.easer.core.data.storage.backend.json.condition;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionData;
import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.storage.C;
import ryey.easer.core.data.storage.backend.Serializer;
import ryey.easer.core.data.storage.backend.UnableToSerializeException;
import ryey.easer.plugins.PluginRegistry;

public class ConditionSerializer implements Serializer<ConditionStructure> {
    @Override
    public String serialize(ConditionStructure data) throws UnableToSerializeException {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(C.NAME, data.getName());
            jsonObject.put(C.VERSION, C.VERSION_CURRENT);
            jsonObject.put(C.CONDITION, serialize_condition(data.getData()));
            return jsonObject.toString();
        } catch (JSONException e) {
            throw new UnableToSerializeException(e.getMessage());
        }
    }

    private JSONObject serialize_condition(ConditionData condition) throws JSONException {
        JSONObject json_situation = new JSONObject();
        json_situation.put(C.SPEC, PluginRegistry.getInstance().condition().findPlugin(condition).id());
        json_situation.put(C.DATA, condition.serialize(PluginDataFormat.JSON));
        return json_situation;
    }
}
