/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.skills.usource.bluetooth_enabled;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.commons.local_skill.eventskill.Slot;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.SkillViewFragment;

public class BluetoothEnabledUSourceSkill implements USourceSkill<BluetoothEnabledUSourceData> {

    @NonNull
    @Override
    public String id() {
        return "bluetooth_enabled";
    }

    @Override
    public int name() {
        return R.string.usource_bluetooth_enabled;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return SkillUtils.checkPermission(context,
                Manifest.permission.BLUETOOTH);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        SkillUtils.requestPermission(activity, requestCode, Manifest.permission.BLUETOOTH);
    }

    @NonNull
    @Override
    public USourceDataFactory<BluetoothEnabledUSourceData> dataFactory() {
        return new BluetoothEnabledUSourceDataFactory();
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.device;
    }

    @NonNull
    @Override
    public SkillViewFragment<BluetoothEnabledUSourceData> view() {
        return new BluetoothEnabledSkillViewFragment();
    }

    @Override
    public Slot<BluetoothEnabledUSourceData> slot(@NonNull Context context, @NonNull BluetoothEnabledUSourceData data) {
        return new BluetoothEnabledSlot(context, data);
    }

    @Override
    public Slot<BluetoothEnabledUSourceData> slot(@NonNull Context context, @NonNull BluetoothEnabledUSourceData data, boolean retriggerable, boolean persistent) {
        return new BluetoothEnabledSlot(context, data, retriggerable, persistent);
    }

    @NonNull
    @Override
    public Tracker<BluetoothEnabledUSourceData> tracker(@NonNull Context context,
                                                        @ValidData @NonNull BluetoothEnabledUSourceData data,
                                                        @NonNull PendingIntent event_positive,
                                                        @NonNull PendingIntent event_negative) {
        return new BluetoothEnabledTracker(context, data, event_positive, event_negative);
    }

}
