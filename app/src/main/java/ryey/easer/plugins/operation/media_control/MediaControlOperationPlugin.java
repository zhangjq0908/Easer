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

package ryey.easer.plugins.operation.media_control;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.operationplugin.Category;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.commons.plugindef.operationplugin.PrivilegeUsage;
import ryey.easer.plugins.reusable.PluginHelper;

public class MediaControlOperationPlugin implements OperationPlugin<MediaControlOperationData> {

    @NonNull
    @Override
    public String id() {
        return "media_control";
    }

    @Override
    public int name() {
        return R.string.operation_media_control;
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
        return 0;
    }

    @NonNull
    @Override
    public Category category() {
        return Category.android;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return isHelperServiceEnabled(context);
        } else {
            return true;
        }
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!isHelperServiceEnabled(activity)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                } else {
                    PluginHelper.requestPermission(activity, requestCode,
                            Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE);
                }
            }
            PluginHelper.reenableComponent(activity, MediaControlHelperNotificationListenerService.class);
        }
    }

    private static boolean isHelperServiceEnabled(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.getComponentEnabledSetting(
                new ComponentName(context, MediaControlHelperNotificationListenerService.class))
                == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    @NonNull
    @Override
    public OperationDataFactory<MediaControlOperationData> dataFactory() {
        return new MediaControlOperationDataFactory();

    }

    @NonNull
    @Override
    public PluginViewFragment<MediaControlOperationData> view() {
        return new MediaControlPluginViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<MediaControlOperationData> loader(@NonNull Context context) {
        return new MediaControlLoader(context);
    }

}
