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

package ryey.easer.core.data.storage.backend.xml.profile;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.io.File;
import java.io.IOException;

import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.backend.IOUtils;

public class XmlProfileDataStorageTest extends ApplicationTestCase<Application> {
    public XmlProfileDataStorageTest() {
        super(Application.class);
    }

    public void testProfileXmlDataStorage() throws IOException {
        File dir = IOUtils.mustGetSubDir(getContext().getFilesDir(), "profile");
        if (dir.exists()) {
            for (String filename : dir.list()) {
                File file = new File(dir, filename);
                assertTrue(file.delete());
            }
            assertTrue(dir.delete());
        }
        XmlProfileDataStorageBackend dataStorage = XmlProfileDataStorageBackend.getInstance(getContext());
        assertTrue(dataStorage.list().isEmpty());
        ProfileStructure profile1 = new ProfileStructure("profile1");
        ProfileStructure profile2 = new ProfileStructure("profile2");
        dataStorage.write(profile1);
        assertTrue(dataStorage.has(profile1.getName()));
        assertEquals(1, dataStorage.list().size());
        assertTrue(dataStorage.has(profile1.getName()));
        assertEquals(1, dataStorage.list().size());
        assertFalse(dataStorage.has(profile2.getName()));
        dataStorage.write(profile2);
        assertTrue(dataStorage.has(profile2.getName()));
        assertEquals(2, dataStorage.list().size());
        try {
            assertEquals(profile1, dataStorage.get("profile1"));
        } catch (ryey.easer.commons.IllegalStorageDataException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(profile2, dataStorage.get("profile2"));
        } catch (ryey.easer.commons.IllegalStorageDataException e) {
            e.printStackTrace();
        }
        assertTrue(dataStorage.has("profile1"));
        dataStorage.delete("profile1");
        assertFalse(dataStorage.has("profile1"));
        assertEquals(1, dataStorage.list().size());
        assertFalse(dataStorage.has("profile1"));
    }
}
