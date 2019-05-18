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

package ryey.easer.commons.local_skill.operationskill;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.Skill;
import ryey.easer.plugin.operation.Category;

/**
 * Definition of every Operation plugin.
 * All Operation skills should implement this interface and write one line to register in `LocalSkillRegistry`.
 * The implementation / subclass doesn't need to hold any data (because data will be passed as arguments to the relevant methods).
 */
public interface OperationSkill<T extends OperationData> extends Skill<T> {

    /**
     * Returns the privilege preference of this plugin.
     */
    @NonNull
    PrivilegeUsage privilege();

    /**
     * Returns the max number of concurrent existence of this plugin in a profile
     * 0 means infinite
     */
    @IntRange(from = 0)
    int maxExistence();

    @NonNull
    Category category();

    @NonNull
    OperationDataFactory<T> dataFactory();

    /**
     * Returns a dummy loader of this plugin.
     * See `OperationLoader` for more information
     */
    @NonNull
    Loader<T> loader(@NonNull Context context);
}
