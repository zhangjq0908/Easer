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

package ryey.easer.commons.plugindef.eventplugin;

import android.content.Context;

import ryey.easer.commons.plugindef.ContentLayout;

/*
 * Definition of every Event plugin.
 * All Event plugins should implement this interface and add one line to register in `PluginRegistry`.
 * The implementation / subclass doesn't need to hold any data (because data will be passed as arguments to the relevant methods).
 */
public interface EventPlugin {
    /*
     * Returns the plugin identifier.
     * Only used internally. Never displayed to user.
     */
    String name();
    /*
     * Returns a dummy (empty) instance of the relevant data structure.
     */
    EventData data();
    /*
     * Returns the control UI of this plugin.
     * Used in `EditEventDialogFragment`.
     */
    ContentLayout view(Context context);
    /*
     * Returns a to-be-initialized Slot of this plugin.
     * See `AbstractSlot` for more information
     */
    AbstractSlot slot(Context context);
}
