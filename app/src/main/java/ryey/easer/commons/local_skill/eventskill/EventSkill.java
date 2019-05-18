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

package ryey.easer.commons.local_skill.eventskill;

import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.ValidData;

/*
 * Definition of every Event plugin.
 * All Event skills should implement this interface and write one line to register in `LocalSkillRegistry`.
 * The implementation / subclass doesn't need to hold any data (because data will be passed as arguments to the relevant methods).
 */
public interface EventSkill<T extends EventData> extends Skill<T> {

    @NonNull
    EventDataFactory<T> dataFactory();

    /**
     * Returns a Slot (reflecting a inline Scenario) of this plugin.
     * See {@link AbstractSlot} for more information
     */
    Slot<T> slot(@NonNull Context context, @ValidData @NonNull T data);

    /**
     * Returns a Slot (reflecting an existing Scenario) of this plugin.
     * See {@link AbstractSlot} for more information
     */
    Slot<T> slot(@NonNull Context context, @ValidData @NonNull T data,
                         boolean retriggerable, boolean persistent);
}
