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

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;

public class FileDataStorageBackendHelper {

    public static <T> T get(Parser<T> parser, File file) throws FileNotFoundException, IllegalStorageDataException {
        try {
            FileInputStream fin = new FileInputStream(file);
            T eventStructure = parser.parse(fin);
            fin.close();
            return eventStructure;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalAccessError();
    }

    public static <T> void write(Serializer<T> serializer, File file, T data) throws IOException {
        try {
            FileOutputStream fout = new FileOutputStream(file);
            String serialized_str = serializer.serialize(data);
            fout.write(serialized_str.getBytes());
            fout.close();
        } catch (UnableToSerializeException e) {
            Logger.e(e, "Unable to serialize");
            e.printStackTrace();
            throw new IOException(e.getMessage());
            //TODO: Maybe throw this exception out?
        }
    }
}
