/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.commons.plugindef.operationplugin;

import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

/**
 * Loader of a operation plugin.
 * Used to perform relevant action given the data (configuration).
 */
public abstract class OperationLoader {
    protected final Context context;

    public OperationLoader(@NonNull Context context) {
        this.context = context;
    }

    public boolean load(@NonNull OperationData data) {
        if (!data.isValid()) {
            Logger.wtf("OperationData <%s> is not valid", data);
            return false;
        }
        boolean ret = _load(data);
        Logger.d("operation data %sloaded", ret ? "" : "not ");
        return ret;
    }

    /**
     * Load the operation given its data.
     * @return whether the load is successful or not.
     */
    protected abstract boolean _load(@NonNull OperationData data);
}
