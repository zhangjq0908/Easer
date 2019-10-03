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

package ryey.easer.commons.local_skill.operationskill;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.ValidData;

/**
 * Loader of an operation plugin.
 * Used to perform relevant action given the data (configuration).
 */
public interface Loader<T extends OperationData> {
    /**
     * Load the operation given its data.
     * The {@link OnResultCallback#onResult(boolean)} method must be called EXACTLY once.
     * @param callback The callback to notify about whether the load is successful or not.
     */
    void load(@ValidData @NonNull T data, @NonNull OnResultCallback callback);

    interface OnResultCallback {
        void onResult(boolean success);
    }
}
