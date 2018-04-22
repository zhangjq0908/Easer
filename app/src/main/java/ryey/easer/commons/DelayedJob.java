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

package ryey.easer.commons;

import android.support.annotation.IntRange;

import com.orhanobut.logger.Logger;

public abstract class DelayedJob {
    private final String TAG;

    private final boolean[] locations;
    private int current_count = 0;

    public DelayedJob(@IntRange(from = 1) int count) {
        this(count, null);
    }

    public DelayedJob(@IntRange(from = 1) int count, String tag) {
        locations = new boolean[count];
        TAG = tag;
    }

    public void tick(int location) {
        if (TAG != null) {
            Logger.d("[%s] tick on %d", TAG, location);
        }
        if (!locations[location]) // We are changing it from false to true
            current_count++;
        locations[location] = true;
        if (current_count >= locations.length) // If all locations are true
            exec();
    }

    public abstract void exec();

}
