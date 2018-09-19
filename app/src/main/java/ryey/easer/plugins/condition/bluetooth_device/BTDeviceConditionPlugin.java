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

package ryey.easer.plugins.condition.bluetooth_device;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.PluginViewFragmentInterface;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionDataFactory;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionPlugin;
import ryey.easer.commons.local_plugin.conditionplugin.Tracker;
import ryey.easer.plugins.reusable.PluginHelper;

public class BTDeviceConditionPlugin implements ConditionPlugin<BTDeviceConditionData> {

    @NonNull
    @Override
    public String id() {
        return "bluetooth device";
    }

    @Override
    public int name() {
        return R.string.condition_bluetooth_device;
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
    public ConditionDataFactory<BTDeviceConditionData> dataFactory() {
        return new BTDeviceConditionDataFactory();
    }

    @NonNull
    @Override
    public PluginViewFragmentInterface<BTDeviceConditionData> view() {
        return new BTDevicePluginViewFragment();
    }

    @NonNull
    @Override
    public Tracker<BTDeviceConditionData> tracker(@NonNull Context context,
                                                 @ValidData @NonNull BTDeviceConditionData data,
                                                 @NonNull PendingIntent event_positive,
                                                 @NonNull PendingIntent event_negative) {
        return new BTDeviceTracker(context, data, event_positive, event_negative);
    }

}
