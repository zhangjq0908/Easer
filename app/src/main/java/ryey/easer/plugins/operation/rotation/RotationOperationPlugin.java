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

package ryey.easer.plugins.operation.rotation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.commons.plugindef.operationplugin.PrivilegeUsage;

public class RotationOperationPlugin implements OperationPlugin {

    @Override
    public String name() {
        return "auto_rotation";
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(context);
        } else {
            return true;
        }
    }

    @Override
    public void requestPermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
        }
    }

    @Override
    public OperationData data() {
        return new RotationOperationData();
    }

    @Override
    public PluginViewFragment view() {
        return new RotationPluginViewFragment();
    }

    @Override
    public OperationLoader loader(Context context) {
        return new RotationLoader(context);
    }
}
