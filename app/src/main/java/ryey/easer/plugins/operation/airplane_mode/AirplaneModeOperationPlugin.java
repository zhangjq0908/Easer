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

package ryey.easer.plugins.operation.airplane_mode;

import android.content.Context;

import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.commons.plugindef.operationplugin.PrivilegeUsage;

public class AirplaneModeOperationPlugin implements OperationPlugin {

    @Override
    public String name() {
        return "airplane_mode";
    }

    @Override
    public PrivilegeUsage privilege() {
        return PrivilegeUsage.prefer_root;
    }

    @Override
    public OperationData data() {
        return new AirplaneModeOperationData();
    }

    @Override
    public PluginViewFragment view() {
        return new AirplaneModePluginViewFragment();
    }

    @Override
    public OperationLoader loader(Context context) {
        return new AirplaneModeLoader(context);
    }
}
