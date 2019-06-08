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

package ryey.easer.skills.condition.wifi_enabled;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.conditionskill.ConditionDataFactory;
import ryey.easer.commons.local_skill.conditionskill.ConditionSkill;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.skills.SkillHelper;
import ryey.easer.skills.SkillViewFragment;

public class WifiEnabledConditionSkill implements ConditionSkill<WifiEnabledConditionData> {

    @NonNull
    @Override
    public String id() {
        return "wifi_enabled";
    }

    @Override
    public int name() {
        return R.string.condition_wifi_enabled;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return SkillHelper.checkPermission(context,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        SkillHelper.requestPermission(activity, requestCode,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE);
    }

    @NonNull
    @Override
    public ConditionDataFactory<WifiEnabledConditionData> dataFactory() {
        return new WifiEnabledConditionDataFactory();
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.device;
    }

    @NonNull
    @Override
    public SkillViewFragment<WifiEnabledConditionData> view() {
        return new WifiEnabledSkillViewFragment();
    }

    @NonNull
    @Override
    public Tracker<WifiEnabledConditionData> tracker(@NonNull Context context,
                                                 @ValidData @NonNull WifiEnabledConditionData data,
                                                 @NonNull PendingIntent event_positive,
                                                 @NonNull PendingIntent event_negative) {
        return new WifiEnabledTracker(context, data, event_positive, event_negative);
    }

}
