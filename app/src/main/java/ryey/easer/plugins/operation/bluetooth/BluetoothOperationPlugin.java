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

package ryey.easer.plugins.operation.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.commons.plugindef.operationplugin.PrivilegeUsage;
import ryey.easer.plugins.reusable.PluginHelper;

public class BluetoothOperationPlugin implements OperationPlugin {

    @Override
    public String name() {
        return "bluetooth";
    }

    @Override
    public PrivilegeUsage privilege() {
        return PrivilegeUsage.no_root;
    }

    @Override
    public int maxExistence() {
        return 1;
    }

    @Override
    public boolean checkPermissions(Context context) {
        return PluginHelper.checkPermission(context,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN);
    }

    @Override
    public void requestPermissions(Activity activity, int requestCode) {
        PluginHelper.requestPermission(activity, requestCode,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN);
    }

    @Override
    public OperationData data() {
        return new BluetoothOperationData();
    }

    @Override
    public PluginViewFragment view() {
        return new BluetoothPluginViewFragment();
    }

    @Override
    public OperationLoader loader(Context context) {
        return new BluetoothLoader(context);
    }
}
