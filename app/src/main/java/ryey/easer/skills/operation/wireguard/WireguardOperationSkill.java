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

package ryey.easer.skills.operation.wireguard;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.content.pm.PackageInfoCompat;

import ryey.easer.R;
import ryey.easer.plugin.operation.Category;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.commons.local_skill.operationskill.OperationDataFactory;
import ryey.easer.skills.operation.OperationLoader;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.commons.local_skill.operationskill.PrivilegeUsage;

public class WireguardOperationSkill implements OperationSkill<WireguardOperationData> {
    static final String WIREGUARD_APP = "com.wireguard.android";
    static final long WIREGUARD_APP_MINIMUM_VERSION = 466; // 0.0.20200322

    static final class permission {
        static final String CONTROL_TUNNELS = "com.wireguard.android.permission.CONTROL_TUNNELS";
    }

    @NonNull
    @Override
    public String id() {
        return "wireguard";
    }

    @Override
    public int name() {
        return R.string.operation_wireguard;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(WIREGUARD_APP, 0);
            return PackageInfoCompat.getLongVersionCode(info) >= WIREGUARD_APP_MINIMUM_VERSION;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
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
        return Category.misc;
    }

    @Override
    public Boolean checkPermissions(@NonNull Context context) {
        return SkillUtils.checkPermission(context, permission.CONTROL_TUNNELS);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        if (!SkillUtils.checkPermission(activity, permission.CONTROL_TUNNELS)) {
            SkillUtils.requestPermission(activity, requestCode, permission.CONTROL_TUNNELS);
        }
    }

    @NonNull
    @Override
    public OperationDataFactory<WireguardOperationData> dataFactory() {
        return new WireguardOperationDataFactory();
    }

    @NonNull
    @Override
    public SkillViewFragment<WireguardOperationData> view() {
        return new WireguardSkillViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<WireguardOperationData> loader(@NonNull Context context) {
        return new WireguardLoader(context);
    }

}
