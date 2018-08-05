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

package ryey.easer.plugins;

import org.junit.Test;

import ryey.easer.commons.C;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.plugindef.DataFactory;
import ryey.easer.commons.plugindef.PluginDef;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.plugins.operation.broadcast.BroadcastOperationPlugin;

import static org.junit.Assert.assertEquals;

public class PluginDataTest {

    @Test
    public void testPluginData() throws Exception {
        for (PluginDef plugin : PluginRegistry.getInstance().all().getAllPlugins()) {
            if (plugin instanceof BroadcastOperationPlugin)
                continue;
            DataFactory factory = plugin.dataFactory();
            StorageData dummyData = factory.dummyData();
            for (PluginDataFormat format : PluginDataFormat.values()) {
                String serialized_data = dummyData.serialize(format);
                StorageData parsed_data = factory.parse(serialized_data, format, C.VERSION_CURRENT);
                assertEquals(dummyData, parsed_data);
            }
        }
    }

}
