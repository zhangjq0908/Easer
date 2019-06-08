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

package ryey.easer.skills.usource.battery;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.event.AbstractSlot;

public class BatteryUSourceSkill implements USourceSkill<BatteryUSourceData> {

    @NonNull
    @Override
    public String id() {
        return "battery";
    }

    @Override
    public int name() {
        return R.string.usource_battery;
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
    public USourceDataFactory<BatteryUSourceData> dataFactory() {
        return new BatteryUSourceDataFactory();
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.device;
    }

    @NonNull
    @Override
    public SkillView<BatteryUSourceData> view() {
        return new BatterySkillViewFragment();
    }

    @Override
    public AbstractSlot<BatteryUSourceData> slot(@NonNull Context context, @ValidData @NonNull BatteryUSourceData data) {
        return new BatterySlot(context, data);
    }

    @Override
    public AbstractSlot<BatteryUSourceData> slot(@NonNull Context context, @NonNull BatteryUSourceData data, boolean retriggerable, boolean persistent) {
        return new BatterySlot(context, data, retriggerable, persistent);
    }

    @Override
    public Tracker<BatteryUSourceData> tracker(@NonNull Context context, @NonNull BatteryUSourceData data, @NonNull PendingIntent event_positive, @NonNull PendingIntent event_negative) {
        return new BatteryTracker(context, data, event_positive, event_negative);
    }

}
