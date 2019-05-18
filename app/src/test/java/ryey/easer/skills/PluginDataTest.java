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

package ryey.easer.skills;

import org.junit.Test;

import ryey.easer.commons.C;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.commons.local_skill.DataFactory;
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.StorageData;
import ryey.easer.skills.operation.broadcast.BroadcastOperationSkill;

import static org.junit.Assert.assertEquals;

public class PluginDataTest {

    @Test
    public void testPluginData() throws Exception {
        for (Skill plugin : LocalSkillRegistry.getInstance().all().getAllSkills()) {
            if (plugin instanceof BroadcastOperationSkill)
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
