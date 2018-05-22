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

package ryey.easer.core.data.storage;

import com.orhanobut.logger.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.core.data.Named;
import ryey.easer.core.data.Verifiable;
import ryey.easer.core.data.WithCreatedVersion;
import ryey.easer.core.data.storage.backend.DataStorageBackendCommonInterface;

public abstract class AbstractDataStorage<T extends Named & Verifiable & WithCreatedVersion, T_backend extends DataStorageBackendCommonInterface<T>> {
    T_backend[] storage_backend_list;

    public List<String> list() {
        List<String> list = null;
        for (T_backend backend : storage_backend_list) {
            if (list == null)
                list = backend.list();
            else
                list.addAll(backend.list());
        }
        return list;
    }

    public T get(String name) {
        for (T_backend backend : storage_backend_list) {
            try {
                return backend.get(name);
            } catch (ryey.easer.commons.IllegalStorageDataException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                Logger.v("data not found on backend <%s>", backend.getClass().getSimpleName());
            }
        }
        return null;
    }

    /**
     * Add a new {@link T} if no existing valid data with the same name exists.
     * @param data The {@link T} going to be added.
     * @return {@code true} if no file conflict, {@code false} otherwise
     * @throws IOException If anything else happens (e.g. no disk space)
     */
    public boolean add(T data) throws IOException {
        for (T_backend backend : storage_backend_list) {
            if (backend.has(data.getName())) {
                try {
                    T existing_data = backend.get(data.getName());
                    if (existing_data.isValid())
                        return false;
                } catch (IllegalStorageDataException ignored) {
                }
                Logger.v("replace an invalid existing data (%s)", data.getName());
                backend.delete(data.getName());
                break;
            }
        }
        storage_backend_list[0].write(data);
        return true;
    }

    abstract boolean isSafeToDelete(String name);

    /**
     * Delete an existing {@link T} with name {@param name}.
     * This method checks whether the data is used by others or not.
     * @param name Name of the data
     * @return {@code true} if {@param name} is safely deleted, {@code false} if it is used by other events.
     */
    public boolean delete(String name) {
        if (!isSafeToDelete(name))
            return false;
        for (T_backend backend : storage_backend_list) {
            if (backend.has(name)) {
                backend.delete(name);
                return true;
            }
        }
        throw new IllegalStateException();
    }

    /**
     * Edit an existing {@link T}, whose name may or may not be changed.
     * @param oldName The name of the data before editing.
     * @param data The {@link T} of the new data (whose name may be different with {@param oldName} because of user's change).
     * @return {@code true} if no name conflict; {@code false} otherwise.
     * @throws IOException See {@link #add(Named)}
     */
    public boolean edit(String oldName, T data) throws IOException {
        if (oldName.equals(data.getName())) {
            update(data);
            return true;
        }
        if (!add(data))
            return false;
        for (T_backend backend : storage_backend_list) {
            if (backend.has(oldName)) {
                backend.delete(oldName);
                return true;
            }
        }
        throw new IllegalAccessError();
    }

    /**
     * Update an existing {@link T} with the new {@param data} without changing the name.
     * @param data The new data which is going to replace the old data
     * @throws IOException See {@link #add(Named)}
     */
    void update(T data) throws IOException {
        String name = data.getName();
        for (T_backend backend : storage_backend_list) {
            if (backend.has(name)) {
                backend.delete(name);
                add(data);
                return;
            }
        }
        throw new IllegalAccessError();
    }
}
