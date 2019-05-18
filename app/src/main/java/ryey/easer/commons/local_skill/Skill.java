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

package ryey.easer.commons.local_skill;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public interface Skill<T extends StorageData> {
    /**
     * Returns the plugin identifier.
     * Only used internally. Never displayed to user.
     */
    @NonNull
    String id();

    /**
     * @return The string resource id of the name (of this plugin)
     */
    @StringRes
    int name();

    /**
     * Check whether this plugin is compatible to the current device.
     * @param context Easer's context
     * @return Whether the plugin is compatible or not
     */
    boolean isCompatible(@NonNull final Context context);

    /**
     * Checks all permission(s) used by this plugin
     * @param context Context object used to check permission
     * @return whether all permissions are satisfied (or not)
     */
    boolean checkPermissions(@NonNull Context context);

    /**
     * Request for all permissions used by this plugin
     * @param activity Activity used to request permissions
     * @param requestCode Request code used to request permissions
     */
    void requestPermissions(@NonNull Activity activity, int requestCode);

    /**
     * Returns a factory to construct the relevant data structure.
     */
    @NonNull
    DataFactory<T> dataFactory();

    /**
     * Returns the control UI of this plugin.
     * Used in relevant UI.
     */
    @NonNull
    SkillView<T> view();
}
