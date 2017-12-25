/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.core.data.storage.backend.xml.event;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.backend.EventDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.plugins.event.wifi.WifiEventData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlEventDataStorageTest {

    final Context context = InstrumentationRegistry.getTargetContext();

    static void compareEventStructure(EventStructure structure1, EventStructure structure2) {
        assertEquals(structure1.getName(), structure2.getName());
        assertEquals(structure1.getProfileName(), structure2.getProfileName());
        assertEquals(structure1.getParentName(), structure2.getParentName());
        assertEquals(structure1.getEventData(), structure2.getEventData());
    }

    @Test
    public void testEventXmlDataStorage() throws ParseException, IOException {
        File dir = IOUtils.mustGetSubDir(context.getFilesDir(), "event");
        if (dir.exists()) {
            for (String filename : dir.list()) {
                File file = new File(dir, filename);
                assertTrue(file.delete());
            }
            assertTrue(dir.delete());
        }
        EventDataStorageBackendInterface dataStorage = XmlEventDataStorageBackend.getInstance(context);
        assertTrue(dataStorage.list().isEmpty());
        EventData eventData = new WifiEventData("testssid");
        eventData.setType(EventType.any);
        EventStructure event1 = new EventStructure("event1");
        event1.setProfileName("profile1");
        event1.setEventData(eventData);
        EventStructure event2 = new EventStructure("event2");
        event2.setProfileName("profile2");
        event2.setEventData(eventData);
        dataStorage.write(event1);
        assertEquals(1, dataStorage.list().size());
        assertTrue(dataStorage.has(event1.getName()));
        assertEquals(1, dataStorage.list().size());
        dataStorage.write(event2);
        assertEquals(2, dataStorage.list().size());
        try {
            compareEventStructure(event1, dataStorage.get("event1"));
        } catch (ryey.easer.commons.IllegalStorageDataException e) {
            e.printStackTrace();
        }
        try {
            compareEventStructure(event2, dataStorage.get("event2"));
        } catch (ryey.easer.commons.IllegalStorageDataException e) {
            e.printStackTrace();
        }
        assertTrue(dataStorage.has(event1.getName()));
        dataStorage.delete("event1");
        assertFalse(dataStorage.has(event1.getName()));
        assertEquals(1, dataStorage.list().size());
        assertFalse(dataStorage.has("event1"));
    }
}
