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

package ryey.easer.core.data;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.plugindef.conditionplugin.ConditionData;

public final class ConditionStructure implements Named, Verifiable, WithCreatedVersion {

    private final int createdVersion;
    @NonNull final String name;
    @NonNull final ConditionData data;

    public ConditionStructure(@NonNull String name, @NonNull ConditionData data) {
        this(C.VERSION_CREATED_IN_RUNTIME, name, data);
    }

    public ConditionStructure(int createdVersion, @NonNull String name, @NonNull ConditionData data) {
        this.createdVersion = createdVersion;
        this.name = name;
        this.data = data;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @NonNull
    public ConditionData getData() {
        return data;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public int createdVersion() {
        return createdVersion;
    }
}
