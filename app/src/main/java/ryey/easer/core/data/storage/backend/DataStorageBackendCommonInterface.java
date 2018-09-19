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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.core.data.Named;

/**
 * All methods won't check clashes (e.g. {@link #write(T)} or existence (e.g. {@link #delete(String)}).
 * They are designed to be checked in {@link ryey.easer.core.data.storage.AbstractDataStorage} and its subclasses.
 * @param <T>
 */
public interface DataStorageBackendCommonInterface<T extends Named> {

    boolean has(String name);

    List<String> list();

    T get(String name) throws FileNotFoundException, IllegalStorageDataException;

    void write(T data) throws IOException;

    void delete(String name);

    List<T> all();
}
