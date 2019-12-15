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

package ryey.easer.skills.usource.battery_level;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.commons.local_skill.eventskill.Slot;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.SkillViewFragment;

public class BatteryLevelUSourceSkill implements USourceSkill<BatteryLevelUSourceData> {

    @NonNull
    @Override
    public String id() {
        return "battery_level";
    }

    @Override
    public int name() {
        return R.string.usource_battery_level;
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.device;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
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
    public USourceDataFactory<BatteryLevelUSourceData> dataFactory() {
        return new BatteryLevelUSourceDataFactory();
    }

    @NonNull
    @Override
    public SkillViewFragment<BatteryLevelUSourceData> view() {
        return new BatteryLevelSkillViewFragment();
    }

    @Override
    public Slot<BatteryLevelUSourceData> slot(@NonNull Context context, @NonNull BatteryLevelUSourceData data) {
        return new BatteryLevelSlot(context, data);
    }

    @Override
    public Slot<BatteryLevelUSourceData> slot(@NonNull Context context, @NonNull BatteryLevelUSourceData data, boolean retriggerable, boolean persistent) {
        return new BatteryLevelSlot(context, data, retriggerable, persistent);
    }

    @NonNull
    @Override
    public Tracker<BatteryLevelUSourceData> tracker(@NonNull Context context,
                                            @ValidData @NonNull BatteryLevelUSourceData data,
                                            @NonNull PendingIntent event_positive,
                                            @NonNull PendingIntent event_negative) {
        return new BatteryLevelTracker(context, data, event_positive, event_negative);
    }

    @Override
    public SkillView<BatteryLevelUSourceData> conditionView() {
        return new BatteryLevelConditionSkillViewFragment();
    }

}
