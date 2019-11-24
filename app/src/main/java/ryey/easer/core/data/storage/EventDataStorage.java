/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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

import androidx.annotation.NonNull;

import java.io.IOException;

import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.storage.backend.EventDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.event.JsonEventDataStorageBackend;

public class EventDataStorage extends AbstractDataStorage<EventStructure, EventDataStorageBackendInterface> {

    public EventDataStorage(@NonNull Context context) {
        super(context, new EventDataStorageBackendInterface[] {
                new JsonEventDataStorageBackend(context),
        });
    }

    @Override
    boolean isSafeToDelete(@NonNull String name) {
        ScriptDataStorage scriptDataStorage = new ScriptDataStorage(context);
        for (ScriptStructure scriptStructure : scriptDataStorage.allScripts()) {
            if (scriptStructure.isEvent() && name.equals(scriptStructure.getEvent().getName()))
                return false;
        }
        return true;
    }

    protected void handleRename(@NonNull String oldName, @NonNull EventStructure newEvent) throws IOException {
        ScriptDataStorage scriptDataStorage = new ScriptDataStorage(context);
        for (ScriptStructure scriptStructure : scriptDataStorage.allScripts()) {
            if (scriptStructure.isEvent() && !scriptStructure.getEvent().isTmpEvent()
                    && oldName.equals(scriptStructure.getEvent().getName())) {
                scriptStructure.setEvent(newEvent);
                scriptDataStorage.update(scriptStructure);
            }
        }
    }
}
