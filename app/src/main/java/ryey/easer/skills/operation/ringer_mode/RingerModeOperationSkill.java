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

package ryey.easer.skills.operation.ringer_mode;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

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

public class RingerModeOperationSkill implements OperationSkill<RingerModeOperationData> {

    @NonNull
    @Override
    public String id() {
        return "ringer_mode";
    }

    @Override
    public int name() {
        return R.string.operation_ringer_mode;
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

    @Nullable
    @Override
    public Boolean checkPermissions(@NonNull Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return SkillUtils.checkPermission(context, Manifest.permission.MODIFY_AUDIO_SETTINGS);
        } else {
            return SkillUtils.checkPermission(context, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                    && SkillUtils.isPermissionGrantedForNotificationListenerService(
                            context, InterruptionFilterSwitcherService.class);
        }
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        if (!SkillUtils.checkPermission(activity, Manifest.permission.MODIFY_AUDIO_SETTINGS))
            SkillUtils.requestPermission(activity, requestCode, Manifest.permission.MODIFY_AUDIO_SETTINGS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!SkillUtils.isPermissionGrantedForNotificationListenerService(activity, InterruptionFilterSwitcherService.class)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    SkillUtils.requestPermission(activity, requestCode,
                            Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE);
                }
                SkillUtils.reenableComponent(activity, InterruptionFilterSwitcherService.class);
            }
        }
    }

    @NonNull
    @Override
    public OperationDataFactory<RingerModeOperationData> dataFactory() {
        return new RingerModeOperationDataFactory();

    }

    @NonNull
    @Override
    public SkillView<RingerModeOperationData> view() {
        return new RingerModeSkillViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<RingerModeOperationData> loader(@NonNull Context context) {
        return new RingerModeLoader(context);
    }

}
