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

import ryey.easer.commons.plugindef.PluginDef;

/**
 * Definition of every Operation plugin.
 * All Operation plugins should implement this interface and write one line to register in `PluginRegistry`.
 * The implementation / subclass doesn't need to hold any data (because data will be passed as arguments to the relevant methods).
 */
public interface OperationPlugin extends PluginDef {

    /**
     * Returns the privilege preference of this plugin.
     */
    PrivilegeUsage privilege();

    /**
     * {@inheritDoc}
     */
    OperationData data();

    /**
     * Returns a dummy loader of this plugin.
     * See `OperationLoader` for more information
     */
    OperationLoader loader(Context context);
}
