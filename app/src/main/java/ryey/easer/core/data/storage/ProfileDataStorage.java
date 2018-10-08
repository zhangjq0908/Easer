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
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.storage.backend.ProfileDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.profile.JsonProfileDataStorageBackend;

public class ProfileDataStorage extends AbstractDataStorage<ProfileStructure, ProfileDataStorageBackendInterface> {

    public ProfileDataStorage(Context context) {
        super(context, new ProfileDataStorageBackendInterface[] {
                new JsonProfileDataStorageBackend(context),
        });
    }

    @Override
    boolean isSafeToDelete(String name) {
        ScriptDataStorage scriptDataStorage = new ScriptDataStorage(context);
        for (ScriptStructure scriptStructure : scriptDataStorage.allScripts()) {
            if (name.equals(scriptStructure.getProfileName()))
                return false;
        }
        return true;
    }

    @Override
    protected void handleRename(String oldName, ProfileStructure profile) throws IOException {
        ScriptDataStorage scriptDataStorage = new ScriptDataStorage(context);
        for (String scriptName : scriptDataStorage.list()) {
            ScriptStructure script = scriptDataStorage.get(scriptName);
            if (oldName.equals(script.getProfileName())) {
                script.setProfileName(profile.getName());
                scriptDataStorage.update(script);
            }
        }
    }


}
