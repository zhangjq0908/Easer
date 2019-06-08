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

package ryey.easer.skills.operation.play_media;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.operationskill.OperationDataFactory;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.commons.local_skill.operationskill.PrivilegeUsage;
import ryey.easer.plugin.operation.Category;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.skills.operation.OperationLoader;
import ryey.easer.skills.SkillHelper;

public class PlayMediaOperationSkill implements OperationSkill<PlayMediaOperationData> {

    @NonNull
    @Override
    public String id() {
        return "play_media";
    }

    @Override
    public int name() {
        return R.string.o_play_media;
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

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return SkillHelper.checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return true;
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            SkillHelper.requestPermission(activity, requestCode, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @NonNull
    @Override
    public OperationDataFactory<PlayMediaOperationData> dataFactory() {
        return new PlayMediaOperationDataFactory();

    }

    @NonNull
    @Override
    public SkillViewFragment<PlayMediaOperationData> view() {
        return new PlayMediaSkillViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<PlayMediaOperationData> loader(@NonNull Context context) {
        return new PlayMediaLoader(context);
    }

}
