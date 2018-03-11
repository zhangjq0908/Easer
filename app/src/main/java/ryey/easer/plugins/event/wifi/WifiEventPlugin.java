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

package ryey.easer.plugins.event.wifi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.plugins.reusable.PluginHelper;

public class WifiEventPlugin implements EventPlugin<WifiEventData> {

    @NonNull
    @Override
    public String id() {
        return "wifi connection";
    }

    @Override
    public int name() {
        return R.string.event_wificonn;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return PluginHelper.checkPermission(context,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        PluginHelper.requestPermission(activity, requestCode,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE);
    }

    @NonNull
    @Override
    public EventDataFactory<WifiEventData> dataFactory() {
        return new WifiEventDataFactory();
    }

    @NonNull
    @Override
    public PluginViewFragment<WifiEventData> view() {
        return new WifiPluginViewFragment();
    }

    @Override
    public AbstractSlot<WifiEventData> slot(@NonNull Context context, @ValidData @NonNull WifiEventData data) {
        return new WifiConnSlot(context, data);
    }

    @Override
    public AbstractSlot<WifiEventData> slot(@NonNull Context context, @NonNull WifiEventData data, boolean retriggerable, boolean persistent) {
        return new WifiConnSlot(context, data, retriggerable, persistent);
    }

}
