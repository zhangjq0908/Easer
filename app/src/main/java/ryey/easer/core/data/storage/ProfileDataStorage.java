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

import android.content.Context;

import java.io.IOException;

import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.backend.ProfileDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.profile.JsonProfileDataStorageBackend;

public class ProfileDataStorage extends AbstractDataStorage<ProfileStructure, ProfileDataStorageBackendInterface> {

    private static ProfileDataStorage instance = null;

    Context context;

    public static ProfileDataStorage getInstance(Context context) {
        if (instance == null) {
            instance = new ProfileDataStorage();
            instance.storage_backend_list = new ProfileDataStorageBackendInterface[] {
                    JsonProfileDataStorageBackend.getInstance(context),
            };
            instance.context = context;
        }
        return instance;
    }

    @Override
    boolean isSafeToDelete(String name) {
        return StorageHelper.isSafeToDeleteProfile(context, name);
    }

    /**
     * Edit an existing {@link ProfileStructure} and handles name changing if any.
     * {@inheritDoc}
     */
    public boolean edit(String oldName, ProfileStructure profile) throws IOException {
        boolean success = super.edit(oldName, profile);
        if (success) {
            if (!oldName.equals(profile.getName())) {
                EventDataStorage eventDataStorage = EventDataStorage.getInstance(context);
                eventDataStorage.handleProfileRename(oldName, profile.getName());
            }
        }
        return success;
    }
}
