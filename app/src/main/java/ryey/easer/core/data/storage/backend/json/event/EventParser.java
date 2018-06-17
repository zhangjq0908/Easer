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

package ryey.easer.core.data.storage.backend.json.event;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.C;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.Parser;
import ryey.easer.plugins.PluginRegistry;

public class EventParser implements Parser<EventStructure> {

    @Override
    public EventStructure parse(InputStream in) throws IOException, IllegalStorageDataException {
        try {
            JSONObject jsonObject = new JSONObject(IOUtils.inputStreamToString(in));
            int version = jsonObject.optInt(C.VERSION, C.VERSION_USE_SCENARIO);
            final String name = jsonObject.getString(C.NAME);
            JSONObject jsonObject_situation = jsonObject.getJSONObject(C.SIT);
            String spec = jsonObject_situation.getString(C.SPEC);
            EventData eventData = PluginRegistry.getInstance().event().findPlugin(spec)
                    .dataFactory()
                    .parse(jsonObject_situation.getString(C.DATA), C.Format.JSON, version);
            return new EventStructure(version, name, eventData);
        } catch (JSONException e) {
            throw new IllegalStorageDataException(e);
        }
    }
}
