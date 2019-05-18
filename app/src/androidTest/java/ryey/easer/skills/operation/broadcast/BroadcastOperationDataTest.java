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

package ryey.easer.skills.operation.broadcast;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.commons.C;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.TestHelper;

import static org.junit.Assert.assertEquals;

public class BroadcastOperationDataTest {

    @Test
    public void testParcel() {
        BroadcastOperationData dummyData = new BroadcastOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BroadcastOperationData parceledData = BroadcastOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

    @Test
    public void testSerialize() throws Exception {
        BroadcastOperationDataFactory factory = new BroadcastOperationDataFactory();
        BroadcastOperationData dummyData = factory.dummyData();
        for (PluginDataFormat format : PluginDataFormat.values()) {
            String serialized_data = dummyData.serialize(format);
            BroadcastOperationData parsed_data = factory.parse(serialized_data, format, C.VERSION_CURRENT);
            assertEquals(dummyData, parsed_data);
        }
    }

}