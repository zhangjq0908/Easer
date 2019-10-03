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

package ryey.easer.skills.operation;

import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.operationskill.Loader;
import ryey.easer.commons.local_skill.operationskill.OperationData;

/**
 * Basic implementation of the {@link Loader} class.
 * Every skill's own Loader should use this class as the base class, unless you are able to handle the callback correctly.
 */
public abstract class OperationLoader<T extends OperationData> implements Loader<T> {
    protected final Context context;

    public OperationLoader(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public final void load(@NonNull T data, @NonNull OnResultCallback callback) {
        final IntermediateCallback intermediateCallback = new IntermediateCallback();
        _load(data, intermediateCallback);
        final Boolean result = intermediateCallback.result;
        if (result == null) {
            callback.onResult(true);
        } else {
            callback.onResult(result);
        }
    }

    public abstract void _load(@NonNull T data, @NonNull OnResultCallback callback);

    private static final class IntermediateCallback implements OnResultCallback {

        private Boolean result = null;

        @Override
        public void onResult(boolean success) {
            result = success;
        }
    }
}
