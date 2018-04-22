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

package ryey.easer.plugins.event.bluetooth_device;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.plugins.reusable.PluginHelper;

public class BTDeviceEventPlugin implements EventPlugin<BTDeviceEventData> {

    @NonNull
    @Override
    public String id() {
        return "bluetooth device";
    }

    @Override
    public int name() {
        return R.string.event_bluetooth_device;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return PluginHelper.checkPermission(context,
                Manifest.permission.BLUETOOTH);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        PluginHelper.requestPermission(activity, requestCode, Manifest.permission.BLUETOOTH);
    }

    @NonNull
    @Override
    public EventDataFactory<BTDeviceEventData> dataFactory() {
        return new BTDeviceEventDataFactory();
    }

    @NonNull
    @Override
    public PluginViewFragment<BTDeviceEventData> view() {
        return new BTDevicePluginViewFragment();
    }

    @Override
    public AbstractSlot<BTDeviceEventData> slot(@NonNull Context context, @ValidData @NonNull BTDeviceEventData data) {
        return new BTDeviceSlot(context, data);
    }

    @Override
    public AbstractSlot<BTDeviceEventData> slot(@NonNull Context context, @NonNull BTDeviceEventData data, boolean retriggerable, boolean persistent) {
        return new BTDeviceSlot(context, data, retriggerable, persistent);
    }

}
