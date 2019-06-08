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

package ryey.easer.skills.event.calendar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.eventskill.EventDataFactory;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.skills.event.AbstractSlot;
import ryey.easer.skills.SkillHelper;

public class CalendarEventSkill implements EventSkill<CalendarEventData> {

    @NonNull
    @Override
    public String id() {
        return "calendar";
    }

    @Override
    public int name() {
        return R.string.event_calendar;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return SkillHelper.checkPermission(context, Manifest.permission.READ_CALENDAR);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        SkillHelper.requestPermission(activity, requestCode, Manifest.permission.READ_CALENDAR);
    }

    @NonNull
    @Override
    public EventDataFactory<CalendarEventData> dataFactory() {
        return new CalendarEventDataFactory();
    }

    @NonNull
    @Override
    public SkillView<CalendarEventData> view() {
        return new CalendarSkillViewFragment();
    }

    @Override
    public AbstractSlot<CalendarEventData> slot(@NonNull Context context, @ValidData @NonNull CalendarEventData data) {
        return new CalendarSlot(context, data);
    }

    @Override
    public AbstractSlot<CalendarEventData> slot(@NonNull Context context, @NonNull CalendarEventData data, boolean retriggerable, boolean persistent) {
        return new CalendarSlot(context, data, retriggerable, persistent);
    }

}
