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

package ryey.easer.commons.local_skill.conditionskill;

import androidx.annotation.Nullable;

/**
 * The basic interface for a Tracker, which tracks the state change of a condition.
 * The implementation should take care of reporting the changed state to upstream.
 * See {@link ryey.easer.skills.condition.SkeletonTracker<D>} if you want to implement a local
 * condition skill.
 * @param <D> The data class for the relevant condition.
 */
public interface Tracker<D extends ConditionData> {

    /**
     * Start functioning as a listener.
     * When the condition becomes true, send the notification to relevant receiver (upstream).
     */
    void start();

    /**
     * Stop functioning as a listener.
     * No longer notify anyone when the condition is satisfied.
     */
    void stop();

    /**
     * @return The current state, or {@code null} representing unknown.
     */
    @Nullable
    Boolean state();

}
