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

import java.io.IOException;
import java.io.InputStream;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.plugindef.conditionplugin.ConditionData;
import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.storage.C;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.Parser;
import ryey.easer.plugins.PluginRegistry;

public class ConditionParser implements Parser<ConditionStructure> {
    @Override
    public ConditionStructure parse(InputStream in) throws IOException, IllegalStorageDataException {
        try {
            JSONObject jsonObject = new JSONObject(IOUtils.inputStreamToString(in));
            int version = jsonObject.optInt(C.VERSION, C.VERSION_USE_SCENARIO);
            final String name = jsonObject.getString(C.NAME);
            JSONObject jsonObject_situation = jsonObject.getJSONObject(C.CONDITION);
            ConditionData conditionData = parse_condition(jsonObject_situation, version);
            return new ConditionStructure(version, name, conditionData);
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e);
        }
    }

    private static ConditionData parse_condition(JSONObject json_condition, int version) throws JSONException, IllegalStorageDataException {
        String spec = json_condition.getString(C.SPEC);
        return PluginRegistry.getInstance().condition().findPlugin(spec)
                .dataFactory()
                .parse(json_condition.getString(C.DATA), PluginDataFormat.JSON, version);
    }
}
