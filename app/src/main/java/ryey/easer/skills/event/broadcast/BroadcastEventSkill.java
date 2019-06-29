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

package ryey.easer.skills.event.broadcast;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.eventskill.EventDataFactory;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.skills.event.AbstractSlot;

public class BroadcastEventSkill implements EventSkill<BroadcastEventData> {

    @NonNull
    @Override
    public String id() {
        return "broadcast";
    }

    @Override
    public int name() {
        return R.string.event_broadcast;
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
    public EventDataFactory<BroadcastEventData> dataFactory() {
        return new BroadcastEventDataFactory();
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.android;
    }

    @NonNull
    @Override
    public SkillView<BroadcastEventData> view() {
        return new BroadcastSkillViewFragment();
    }

    @Override
    public AbstractSlot<BroadcastEventData> slot(@NonNull Context context, @ValidData @NonNull BroadcastEventData data) {
        return new BroadcastConnSlot(context, data);
    }

    @Override
    public AbstractSlot<BroadcastEventData> slot(@NonNull Context context, @NonNull BroadcastEventData data, boolean retriggerable, boolean persistent) {
        return new BroadcastConnSlot(context, data, retriggerable, persistent);
    }

}
