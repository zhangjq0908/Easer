/*
 * Copyright (c) 2016 - 2022 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.core.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

public class HelperTest {

    public static final String ASSET_NAME_EXPORTED_OK_FILE = "test-data-ok.zip";

    public static final String FILENAME_EXPORTED = "test-export.zip";

    @BeforeClass
    public static void setUpDirs() {
        File filesDir = InstrumentationRegistry.getInstrumentation().getTargetContext().getFilesDir();
        assertTrue(filesDir.exists());
        assertTrue(filesDir.isDirectory());
        for (String dirName : new String[]{"condition", "event", "profile", "script"}) {
            File dir = new File(filesDir, dirName);
            assertTrue(dir.exists() && dir.isDirectory() || dir.mkdir());
        }
    }

    @Test
    public void testImportExport() throws Exception {
        InputStream inputStream = InstrumentationRegistry.getInstrumentation()
                .getContext().getAssets().open(ASSET_NAME_EXPORTED_OK_FILE);
        Helper.import_data(InstrumentationRegistry.getInstrumentation().getTargetContext(), inputStream);

        File[] files = InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getFilesDir().listFiles();
        assertNotNull(files);
        assertTrue(files.length > 0);

        File outputDir = InstrumentationRegistry.getInstrumentation().getTargetContext().getFilesDir();
        File outputFile = new File(outputDir, FILENAME_EXPORTED);
        Helper.export_data(InstrumentationRegistry.getInstrumentation().getTargetContext(), outputFile);
        assertTrue(outputFile.length() > 0);
    }

    @AfterClass
    public static void cleanUpData() {
        File filesDir = InstrumentationRegistry.getInstrumentation().getTargetContext().getFilesDir();
        assertTrue(filesDir.exists());
        assertTrue(filesDir.isDirectory());
        for (String dirName : new String[]{"condition", "event", "profile", "script"}) {
            File dir = new File(filesDir, dirName);
            File[] files = dir.listFiles();
            assertNotNull(files);
            for (File dataFile : files) {
                assertTrue(dataFile.delete());
            }
            assertTrue(dir.delete());
        }
    }

    @AfterClass
    public static void cleanUpExported() {
        File outputDir = InstrumentationRegistry.getInstrumentation().getTargetContext().getFilesDir();
        File outputFile = new File(outputDir, FILENAME_EXPORTED);
        assertTrue(outputFile.delete());
    }
}
