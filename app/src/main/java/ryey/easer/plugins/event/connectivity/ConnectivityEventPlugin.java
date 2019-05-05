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

package ryey.easer.plugins.event.connectivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.PluginViewFragmentInterface;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.commons.local_plugin.eventplugin.EventDataFactory;
import ryey.easer.commons.local_plugin.eventplugin.EventPlugin;
import ryey.easer.plugins.event.AbstractSlot;
import ryey.easer.plugins.reusable.PluginHelper;

@Deprecated
public class ConnectivityEventPlugin implements EventPlugin<ConnectivityEventData> {

    @NonNull
    @Override
    public String id() {
        return "connectivity";
    }

    @Override
    public int name() {
        return R.string.event_connectivity;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return PluginHelper.checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        PluginHelper.requestPermission(activity, requestCode, Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @NonNull
    @Override
    public EventDataFactory<ConnectivityEventData> dataFactory() {
        return new ConnectivityEventDataFactory();
    }

    @NonNull
    @Override
    public PluginViewFragmentInterface<ConnectivityEventData> view() {
        return new ConnectivityPluginViewFragment();
    }

    @Override
    public AbstractSlot<ConnectivityEventData> slot(@NonNull Context context, @ValidData @NonNull ConnectivityEventData data) {
        return new ConnectivitySlot(context, data);
    }

    @Override
    public AbstractSlot<ConnectivityEventData> slot(@NonNull Context context, @NonNull ConnectivityEventData data, boolean retriggerable, boolean persistent) {
        return new ConnectivitySlot(context, data, retriggerable, persistent);
    }

}
