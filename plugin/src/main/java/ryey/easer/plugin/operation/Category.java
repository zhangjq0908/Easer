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

package ryey.easer.plugin.operation;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import ryey.easer.plugin.R;


public enum Category {
    android,
    system_config,
    easer,
    misc,
    unknown;

    @NonNull
    public String toString(Resources resources) {
        switch (this) {
            case android:
                return resources.getString(R.string.category_operation_android);
            case system_config:
                return resources.getString(R.string.category_operation_system_config);
            case easer:
                return resources.getString(R.string.category_operation_easer);
            case misc:
                return resources.getString(R.string.category_operation_misc);
            case unknown:
                return resources.getString(R.string.category_operation_unknown);
        }
        throw new IllegalStateException("Category isn't caught in switch statement");
    }
}
