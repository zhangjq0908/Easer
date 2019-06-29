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

package ryey.easer.skills.operation.wifi;

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

public class WifiOperationSkill implements OperationSkill<WifiOperationData> {

    @NonNull
    @Override
    public String id() {
        return "wifi";
    }

    @Override
    public int name() {
        return R.string.operation_wifi;
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

    @Nullable
    @Override
    public Boolean checkPermissions(@NonNull Context context) {
        return SkillUtils.checkPermission(context,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        boolean can_access_wifi = SkillUtils.checkPermission(activity, Manifest.permission.ACCESS_WIFI_STATE);
        boolean can_change_wifi = SkillUtils.checkPermission(activity, Manifest.permission.CHANGE_WIFI_STATE);
        if (!can_access_wifi && !can_change_wifi) {
            SkillUtils.requestPermission(activity, requestCode,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE);
        } else if (!can_access_wifi) {
            SkillUtils.requestPermission(activity, requestCode,
                    Manifest.permission.ACCESS_WIFI_STATE);
        } else {
            SkillUtils.requestPermission(activity, requestCode,
                    Manifest.permission.CHANGE_WIFI_STATE);
        }
    }

    @NonNull
    @Override
    public OperationDataFactory<WifiOperationData> dataFactory() {
        return new WifiOperationDataFactory();

    }

    @NonNull
    @Override
    public SkillView<WifiOperationData> view() {
        return new WifiSkillViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<WifiOperationData> loader(@NonNull Context context) {
        return new WifiLoader(context);
    }

}
