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

package ryey.easer.plugins.operation.brightness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.commons.plugindef.operationplugin.PrivilegeUsage;

public class BrightnessOperationPlugin implements OperationPlugin {

    @NonNull
    @Override
    public String id() {
        return "brightness";
    }

    @Override
    public int name() {
        return R.string.operation_brightness;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @NonNull
    @Override
    public PrivilegeUsage privilege() {
        return PrivilegeUsage.no_root;
    }

    @Override
    public int maxExistence() {
        return 1;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(context);
        } else {
            return true;
        }
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
        }
    }

    @NonNull
    @Override
    public OperationDataFactory dataFactory() {
        return new BrightnessOperationDataFactory();

    }

    @NonNull
    @Override
    public PluginViewFragment view() {
        return new BrightnessPluginViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader loader(@NonNull Context context) {
        return new BrightnessLoader(context);
    }

}
