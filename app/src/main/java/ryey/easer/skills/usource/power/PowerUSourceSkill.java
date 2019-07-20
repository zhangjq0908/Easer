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

package ryey.easer.skills.usource.power;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.event.AbstractSlot;

public class PowerUSourceSkill implements USourceSkill<PowerUSourceData> {

    @NonNull
    @Override
    public String id() {
        return "power_status";
    }

    @Override
    public int name() {
        return R.string.usource_power;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Nullable
    @Override
    public Boolean checkPermissions(@NonNull Context context) {
        return null;
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {

    }

    @NonNull
    @Override
    public USourceDataFactory<PowerUSourceData> dataFactory() {
        return new PowerUSourceDataFactory();
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.device;
    }

    @NonNull
    @Override
    public SkillView<PowerUSourceData> view() {
        return new PowerSkillViewFragment();
    }

    @Override
    public AbstractSlot<PowerUSourceData> slot(@NonNull Context context, @ValidData @NonNull PowerUSourceData data) {
        return new PowerSlot(context, data);
    }

    @Override
    public AbstractSlot<PowerUSourceData> slot(@NonNull Context context, @NonNull PowerUSourceData data, boolean retriggerable, boolean persistent) {
        return new PowerSlot(context, data, retriggerable, persistent);
    }

    @Override
    public Tracker<PowerUSourceData> tracker(@NonNull Context context, @NonNull PowerUSourceData data, @NonNull PendingIntent event_positive, @NonNull PendingIntent event_negative) {
        return new PowerTracker(context, data, event_positive, event_negative);
    }

}
