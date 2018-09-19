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

package ryey.easer.plugins.reusable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ryey.easer.commons.local_plugin.StorageData;

public abstract class IntegerData implements StorageData {
    protected Integer level = null;
    protected Integer lbound = null;
    protected Integer rbound = null;

    protected IntegerData() {}

    protected IntegerData(@NonNull Integer level) {
        this.level = level;
    }

    @NonNull
    public Integer get() {
        return level;
    }

    public void set(int level) {
        this.level = level;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (level == null)
            return false;
        if (lbound != null)
            if (level < lbound)
                return false;
        if (rbound != null)
            if (level > rbound)
                return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (!getClass().equals(obj.getClass()))
            return false;
        if (!isValid() || !((StorageData) obj).isValid())
            return false;
        if (((IntegerData) obj).lbound != lbound.intValue())
            return false;
        if (((IntegerData) obj).rbound != rbound.intValue())
            return false;
        if (((IntegerData) obj).level != level.intValue())
            return false;
        return true;
    }
}
