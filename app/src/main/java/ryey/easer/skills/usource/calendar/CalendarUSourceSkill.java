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

package ryey.easer.skills.usource.calendar;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.commons.local_skill.eventskill.Slot;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.SkillUtils;

public class CalendarUSourceSkill implements USourceSkill<CalendarUSourceData> {
    @NonNull
    @Override
    public String id() {
        return "calendar";
    }

    @Override
    public int name() {
        return R.string.usource_calendar;
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.personal;
    }

    @Override
    public boolean isCompatible(@NonNull Context context) {
        return true;
    }

    @Nullable
    @Override
    public Boolean checkPermissions(@NonNull Context context) {
        return SkillUtils.checkPermission(context, Manifest.permission.READ_CALENDAR);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        SkillUtils.requestPermission(activity, requestCode, Manifest.permission.READ_CALENDAR);
    }

    @NonNull
    @Override
    public USourceDataFactory<CalendarUSourceData> dataFactory() {
        return new CalendarUSourceDataFactory();
    }

    @NonNull
    @Override
    public SkillView<CalendarUSourceData> view() {
        return new CalendarSkillViewFragment();
    }

    @Override
    public Slot<CalendarUSourceData> slot(@NonNull Context context, @NonNull CalendarUSourceData data) {
        return new CalendarSlot(context, data);
    }

    @Override
    public Slot<CalendarUSourceData> slot(@NonNull Context context, @NonNull CalendarUSourceData data, boolean retriggerable, boolean persistent) {
        return new CalendarSlot(context, data, retriggerable, persistent);
    }

    @Override
    public Tracker<CalendarUSourceData> tracker(@NonNull Context context, @NonNull CalendarUSourceData data, @NonNull PendingIntent event_positive, @NonNull PendingIntent event_negative) {
        return new CalendarTracker(context, data, event_positive, event_negative);
    }
}
