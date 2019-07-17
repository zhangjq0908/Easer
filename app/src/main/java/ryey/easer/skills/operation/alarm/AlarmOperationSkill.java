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

package ryey.easer.skills.operation.alarm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.operationskill.OperationDataFactory;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.commons.local_skill.operationskill.PrivilegeUsage;
import ryey.easer.plugin.operation.Category;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.operation.OperationLoader;

public class AlarmOperationSkill implements OperationSkill<AlarmOperationData> {

    @NonNull
    @Override
    public String id() {
        return "alarm";
    }

    @Override
    public int name() {
        return R.string.operation_alarm;
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
        return Category.android;
    }

    @Nullable
    @Override
    public Boolean checkPermissions(@NonNull Context context) {
        return SkillUtils.checkPermission(context, Manifest.permission.SET_ALARM);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        SkillUtils.requestPermission(activity, requestCode, Manifest.permission.SET_ALARM);
    }

    @NonNull
    @Override
    public OperationDataFactory<AlarmOperationData> dataFactory() {
        return new AlarmOperationDataFactory();

    }

    @NonNull
    @Override
    public SkillView<AlarmOperationData> view() {
        return new AlarmSkillViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<AlarmOperationData> loader(@NonNull Context context) {
        return new AlarmLoader(context);
    }

}
