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

package ryey.easer.plugins.event.notification;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;

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
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return NotificationEventListenerService.isRunning();
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        PackageManager pm = activity.getPackageManager();
        ComponentName componentName = new ComponentName(activity, NotificationEventListenerService.class);

        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @NonNull
    @Override
    public EventDataFactory<NotificationEventData> dataFactory() {
        return new NotificationEventDataFactory();

    }

    @NonNull
    @Override
    public PluginViewFragment<NotificationEventData> view() {
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
