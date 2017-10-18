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

package ryey.easer.core.data.storage.xml.event;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.EventDataStorage;
import ryey.easer.core.data.storage.FileUtils;
import ryey.easer.plugins.event.wifi.WifiEventData;

public class XmlEventDataStorageTest extends ApplicationTestCase<Application> {

    public XmlEventDataStorageTest() {
        super(Application.class);
    }

    void compareEventStructure(EventStructure structure1, EventStructure structure2) {
        assertEquals(structure1.getName(), structure2.getName());
        assertEquals(structure1.getProfileName(), structure2.getProfileName());
        assertEquals(structure1.getParentName(), structure2.getParentName());
        assertEquals(structure1.getEventData(), structure2.getEventData());
    }

    @Test
    public void testEventXmlDataStorage() throws IOException, ParseException {
        File dir = FileUtils.getSubDir(getContext().getFilesDir(), "event");
        if (dir.exists()) {
            for (String filename : dir.list()) {
                File file = new File(dir, filename);
                assertTrue(file.delete());
            }
            assertTrue(dir.delete());
        }
        EventDataStorage dataStorage = XmlEventDataStorage.getInstance(getContext());
        assertTrue(dataStorage.list().isEmpty());
        EventData eventData = new WifiEventData("testssid");
        eventData.setType(EventType.any);
        EventStructure event1 = new EventStructure("event1");
        event1.setProfileName("profile1");
        event1.setEventData(eventData);
        EventStructure event2 = new EventStructure("event2");
        event2.setProfileName("profile2");
        event2.setEventData(eventData);
        assertTrue(dataStorage.add(event1));
        assertEquals(1, dataStorage.list().size());
        assertFalse(dataStorage.add(event1));
        assertEquals(1, dataStorage.list().size());
        assertTrue(dataStorage.add(event2));
        assertEquals(2, dataStorage.list().size());
        compareEventStructure(event1, dataStorage.get("event1"));
        compareEventStructure(event2, dataStorage.get("event2"));
        assertTrue(dataStorage.delete("event1"));
        assertEquals(1, dataStorage.list().size());
        assertNull(dataStorage.get("event1"));
    }
}
