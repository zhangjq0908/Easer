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

import java.util.Locale;

import ryey.easer.commons.plugindef.PluginDef;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;

public class CommonHelper {
    public static String pluginEnabledKey(PluginDef plugin) {
        String type_name;
        if (plugin instanceof OperationPlugin)
            type_name = "operation";
        else if (plugin instanceof EventPlugin)
            type_name = "event";
        else
            throw new IllegalAccessError();
        return String.format(Locale.US, "%s_plugin_enabled_%s", type_name, plugin.name());
    }
}
