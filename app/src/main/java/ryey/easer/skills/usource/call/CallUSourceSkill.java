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

package ryey.easer.skills.usource.call;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.commons.local_skill.eventskill.Slot;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.SkillViewFragment;

public class CallUSourceSkill implements USourceSkill<CallUSourceData> {

    @NonNull
    @Override
    public String id() {
        return "call";
    }

    @Override
    public int name() {
        return R.string.usource_call;
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.personal;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public Boolean checkPermissions(@NonNull Context context) {
        return SkillUtils.checkPermission(context,
                Manifest.permission.READ_CALL_LOG );
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        SkillUtils.requestPermission(activity, requestCode, Manifest.permission.READ_CALL_LOG);
    }

    @NonNull
    @Override
    public USourceDataFactory<CallUSourceData> dataFactory() {
        return new CallUSourceDataFactory();
    }

    @NonNull
    @Override
    public SkillViewFragment<CallUSourceData> view() {
        return new CallSkillViewFragment();
    }

    @Override
    public Slot<CallUSourceData> slot(@NonNull Context context, @NonNull CallUSourceData data) {
        return new CallSlot(context, data);
    }

    @Override
    public Slot<CallUSourceData> slot(@NonNull Context context, @NonNull CallUSourceData data, boolean retriggerable, boolean persistent) {
        return new CallSlot(context, data, retriggerable, persistent);
    }

    @NonNull
    @Override
    public Tracker<CallUSourceData> tracker(@NonNull Context context,
                                            @ValidData @NonNull CallUSourceData data,
                                            @NonNull PendingIntent event_positive,
                                            @NonNull PendingIntent event_negative) {
        return new CallTracker(context, data, event_positive, event_negative);
    }

}
