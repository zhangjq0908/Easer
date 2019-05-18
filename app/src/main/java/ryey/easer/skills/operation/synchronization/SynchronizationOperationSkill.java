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

package ryey.easer.skills.operation.synchronization;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.operationskill.OperationDataFactory;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.commons.local_skill.operationskill.PrivilegeUsage;
import ryey.easer.plugin.operation.Category;
import ryey.easer.skills.operation.OperationLoader;
import ryey.easer.skills.reusable.PluginHelper;

public class SynchronizationOperationSkill implements OperationSkill<SynchronizationOperationData> {

    @NonNull
    @Override
    public String id() {
        return "synchronization";
    }

    @Override
    public int name() {
        return R.string.operation_synchronization;
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
        return 1;
    }

    @NonNull
    @Override
    public Category category() {
        return Category.system_config;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return PluginHelper.checkPermission(context, Manifest.permission.WRITE_SYNC_SETTINGS);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        PluginHelper.requestPermission(activity, requestCode, Manifest.permission.WRITE_SYNC_SETTINGS);
    }

    @NonNull
    @Override
    public OperationDataFactory<SynchronizationOperationData> dataFactory() {
        return new SynchronizationOperationDataFactory();

    }

    @NonNull
    @Override
    public SkillView<SynchronizationOperationData> view() {
        return new SynchronizationSkillViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<SynchronizationOperationData> loader(@NonNull Context context) {
        return new SynchronizationLoader(context);
    }

}
