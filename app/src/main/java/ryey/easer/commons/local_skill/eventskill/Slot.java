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

import android.net.Uri;

public interface Slot<T extends EventData> {
    /**
     * Start functioning as a top-level listener.
     * When (probably satisfying) event happens, notify itself and finally proceed to {@link #changeSatisfiedState(boolean)}.
     *
     * Can be called multiply times (data remain the same).
     */
    void listen();

    /**
     * Stop functioning as a top-level listener.
     * No longer notify itself even when it is really satisfied.
     */
    void cancel();

    void register(Uri data);
}
