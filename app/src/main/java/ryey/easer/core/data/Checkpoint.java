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

package ryey.easer.core.data;

import android.support.annotation.NonNull;

/**
 * This class represents a CheckPoint in the background state (i.e. the collection of all Scripts and their dependencies)
 *
 * Currently it is a projection from ScriptTree
 * TODO: Use this class instead of Script in everywhere (ScriptTree, EHService, Lotus, OverviewFragment, TODO)
 * TODO: Replace most of Script's role
 *
 * TODO: Convert relevant interfaces to Kotlin so that this class can be written in kotlin
 */
public class Checkpoint implements Named, Verifiable {

    @NonNull
    public static Checkpoint fromScript(@NonNull ScriptStructure scriptStructure) {
        String name = scriptStructure.name;
        boolean enabled = scriptStructure.active;
        boolean valid = scriptStructure.isValid();
        return new Checkpoint(name, enabled, valid, scriptStructure.isCondition());
    }

    @NonNull
    public final String name;
    public final boolean enabled;
    public final boolean valid;
    public final boolean isCondition;

    public Checkpoint(@NonNull String name, boolean enabled, boolean valid, boolean isCondition) {
        this.name = name;
        this.enabled = enabled;
        this.valid = valid;
        this.isCondition = isCondition;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
