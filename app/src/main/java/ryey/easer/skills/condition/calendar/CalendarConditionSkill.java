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

package ryey.easer.skills.condition.calendar;

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
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.SkillViewFragment;

public class CalendarConditionSkill implements ConditionSkill<CalendarConditionData> {

    @NonNull
    @Override
    public String id() {
        return "calendar_condition";
    }

    @Override
    public int name() { return R.string.event_calendar; }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return SkillUtils.checkPermission(context, Manifest.permission.READ_CALENDAR);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        SkillUtils.requestPermission(activity, requestCode, Manifest.permission.READ_CALENDAR);
    }

    @NonNull
    @Override
    public ConditionDataFactory<CalendarConditionData> dataFactory() {
        return new CalendarConditionDataFactory();
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.personal;
    }

    @NonNull
    @Override
    public SkillViewFragment<CalendarConditionData> view() {
        return new CalendarSkillViewFragment();
    }

    @NonNull
    @Override
    public Tracker<CalendarConditionData> tracker(@NonNull Context context,
                                                 @ValidData @NonNull CalendarConditionData data,
                                                 @NonNull PendingIntent event_positive,
                                                 @NonNull PendingIntent event_negative) {
        return new CalendarTracker(context, data, event_positive, event_negative);
    }

}
