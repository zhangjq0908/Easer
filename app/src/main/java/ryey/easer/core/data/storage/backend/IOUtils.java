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

package ryey.easer.core.data.storage.backend;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

    public static String inputStreamToString(InputStream in) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    public static File mustGetSubDir(File dir, String sub) {
        File subdir;
        subdir = new File(dir, sub);
        if (subdir.exists())
            if (!subdir.isDirectory())
                throw new IllegalStateException("Given path exists and is not a dir:" + sub);
            else
                return subdir;
        else
            if (!subdir.mkdir())
                throw new IllegalStateException("Unable to create subdir in the given path:" + sub);
            else
                return subdir;
    }

    public static boolean fileExists(File dir, String name) {
        File file = new File(dir, name);
        if (file.exists()) {
            if (file.isFile())
                return true;
            else
                throw new IllegalStateException("File exists but is not a file");
        }
        return false;
    }
}
