/*
 * Copyright (c) 2016 - 2018, 2020 Rui Zhao <renyuneyun@gmail.com> and Daniel Landau
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

package ryey.easer.skills.operation.bluetooth_connect;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.plugin.operation.Category;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.commons.local_skill.operationskill.OperationDataFactory;
import ryey.easer.skills.operation.OperationLoader;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.commons.local_skill.operationskill.PrivilegeUsage;

public class BluetoothConnectOperationSkill implements OperationSkill<BluetoothConnectOperationData> {

    @NonNull
    @Override
    public String id() {
        return "bluetooth_connect";
    }

    @Override
    public int name() {
        return R.string.operation_bluetooth_connect;
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
        return Category.system_config;
    }

    @Override
    public Boolean checkPermissions(@NonNull Context context) {
        return true;
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
    }

    @NonNull
    @Override
    public OperationDataFactory<BluetoothConnectOperationData> dataFactory() {
        return new BluetoothConnectOperationDataFactory();

    }

    @NonNull
    @Override
    public SkillViewFragment<BluetoothConnectOperationData> view() {
        return new BluetoothConnectSkillViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<BluetoothConnectOperationData> loader(@NonNull Context context) {
        return new BluetoothConnectLoader(context);
    }

}
