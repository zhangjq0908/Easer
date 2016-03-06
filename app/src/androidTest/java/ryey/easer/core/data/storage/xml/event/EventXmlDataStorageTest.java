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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.FileUtils;

public class EventXmlDataStorageTest extends ApplicationTestCase<Application> {
    public EventXmlDataStorageTest() {
        super(Application.class);
    }

    public void testEventXmlDataStorage() throws IOException, ParseException {
        File dir = FileUtils.getSubDir(getContext().getFilesDir(), "event");
        if (dir.exists()) {
            for (String filename : dir.list()) {
                File file = new File(dir, filename);
                assertTrue(file.delete());
            }
            assertTrue(dir.delete());
        }
        EventXmlDataStorage dataStorage = EventXmlDataStorage.getInstance(getContext());
        assertTrue(dataStorage.list().isEmpty());
        EventStructure event1 = new EventStructure("event1");
        event1.setProfile("profile1");
        event1.setEnabled(true);
//        event1.setTime(Utils.TextToTime("13:47"));
        EventStructure event2 = new EventStructure("event2");
        event2.setProfile("profile2");
//        event2.setTime(Utils.TextToTime("14:57"));
        assertTrue(dataStorage.add(event1));
        assertEquals(1, dataStorage.list().size());
        assertFalse(dataStorage.add(event1));
        assertEquals(1, dataStorage.list().size());
        assertTrue(dataStorage.add(event2));
        assertEquals(2, dataStorage.list().size());
        assertEquals(event1, dataStorage.get("event1"));
        assertEquals(event2, dataStorage.get("event2"));
        assertTrue(dataStorage.delete("event1"));
        assertEquals(1, dataStorage.list().size());
        assertNull(dataStorage.get("event1"));
    }
}
