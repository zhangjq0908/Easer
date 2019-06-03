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

package ryey.easer.skills.usource.time;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.event.AbstractSlot;

public class TimeUSourceSkill implements USourceSkill<TimeUSourceData> {

    @NonNull
    @Override
    public String id() {
        return "time";
    }

    @Override
    public int name() {
        return R.string.event_time;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return true;
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {

    }

    @NonNull
    @Override
    public USourceDataFactory<TimeUSourceData> dataFactory() {
        return new TimeUSourceDataFactory();

    }

    @NonNull
    @Override
    public SkillView<TimeUSourceData> view() {
        return new TimeSkillViewFragment();
    }

    @Override
    public SkillView<TimeUSourceData> eventView() {
        return new TimeEventSkillViewFragment();
    }

    @Override
    public AbstractSlot<TimeUSourceData> slot(@NonNull Context context, @ValidData @NonNull TimeUSourceData data) {
        return new TimeSlot(context, data);
    }

    @Override
    public AbstractSlot<TimeUSourceData> slot(@NonNull Context context, @NonNull TimeUSourceData data, boolean retriggerable, boolean persistent) {
        return new TimeSlot(context, data, retriggerable, persistent);
    }

    @Override
    public Tracker<TimeUSourceData> tracker(@NonNull Context context, @NonNull TimeUSourceData data, @NonNull PendingIntent event_positive, @NonNull PendingIntent event_negative) {
        return new TimeTracker(context, data, event_positive, event_negative);
    }

}
