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

package ryey.easer.plugins.event.notification;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.PluginViewFragmentInterface;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.event.AbstractSlot;
import ryey.easer.commons.local_plugin.eventplugin.EventDataFactory;
import ryey.easer.commons.local_plugin.eventplugin.EventPlugin;
import ryey.easer.plugins.reusable.PluginHelper;

public class NotificationEventPlugin implements EventPlugin<NotificationEventData> {

    @NonNull
    @Override
    public String id() {
        return "notification";
    }

    @Override
    public int name() {
        return R.string.event_notification;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        //TODO support Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return isServiceEnabled(context);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        if (!isServiceEnabled(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            } else {
                PluginHelper.requestPermission(activity, requestCode,
                        Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE);
            }
        }
        PluginHelper.reenableComponent(activity, NotificationEventListenerService.class);
    }

    private static boolean isServiceEnabled(Context context) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, NotificationEventListenerService.class);
        return pm.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    @NonNull
    @Override
    public EventDataFactory<NotificationEventData> dataFactory() {
        return new NotificationEventDataFactory();

    }

    @NonNull
    @Override
    public PluginViewFragmentInterface<NotificationEventData> view() {
        return new NotificationPluginViewFragment();
    }

    @Override
    public AbstractSlot<NotificationEventData> slot(@NonNull Context context, @ValidData @NonNull NotificationEventData data) {
        return new NotificationSlot(context, data);
    }

    @Override
    public AbstractSlot<NotificationEventData> slot(@NonNull Context context, @NonNull NotificationEventData data, boolean retriggerable, boolean persistent) {
        return new NotificationSlot(context, data, retriggerable, persistent);
    }

}
