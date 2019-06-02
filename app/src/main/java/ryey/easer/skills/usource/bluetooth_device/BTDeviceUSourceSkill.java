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

package ryey.easer.skills.usource.bluetooth_device;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.event.AbstractSlot;
import ryey.easer.skills.reusable.PluginHelper;

public class BTDeviceUSourceSkill implements USourceSkill<BTDeviceUSourceData> {

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
    public USourceDataFactory<BTDeviceUSourceData> dataFactory() {
        return new BTDeviceUSourceDataFactory();
    }

    @NonNull
    @Override
    public SkillView<BTDeviceUSourceData> view() {
        return new BTDeviceSkillViewFragment();
    }

    @Override
    public AbstractSlot<BTDeviceUSourceData> slot(@NonNull Context context, @ValidData @NonNull BTDeviceUSourceData data) {
        return new BTDeviceSlot(context, data);
    }

    @Override
    public AbstractSlot<BTDeviceUSourceData> slot(@NonNull Context context, @NonNull BTDeviceUSourceData data, boolean retriggerable, boolean persistent) {
        return new BTDeviceSlot(context, data, retriggerable, persistent);
    }

    @Override
    public Tracker<BTDeviceUSourceData> tracker(@NonNull Context context, @NonNull BTDeviceUSourceData data, @NonNull PendingIntent event_positive, @NonNull PendingIntent event_negative) {
        return new BTDeviceTracker(context, data, event_positive, event_negative);
    }

}
