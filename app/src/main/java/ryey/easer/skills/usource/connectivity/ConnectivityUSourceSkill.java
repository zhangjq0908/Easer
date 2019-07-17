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

package ryey.easer.skills.usource.connectivity;

import android.Manifest;
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
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.event.AbstractSlot;

public class ConnectivityUSourceSkill implements USourceSkill<ConnectivityEventData> {

    @NonNull
    @Override
    public String id() {
        return "connectivity";
    }

    @Override
    public int name() {
        return R.string.usource_connectivity;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Nullable
    @Override
    public Boolean checkPermissions(@NonNull Context context) {
        return SkillUtils.checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        SkillUtils.requestPermission(activity, requestCode, Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @NonNull
    @Override
    public USourceDataFactory<ConnectivityEventData> dataFactory() {
        return new ConnectivityEventDataFactory();
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.device;
    }

    @NonNull
    @Override
    public SkillView<ConnectivityEventData> view() {
        return new ConnectivitySkillViewFragment();
    }

    @Override
    public AbstractSlot<ConnectivityEventData> slot(@NonNull Context context, @ValidData @NonNull ConnectivityEventData data) {
        return new ConnectivitySlot(context, data);
    }

    @Override
    public AbstractSlot<ConnectivityEventData> slot(@NonNull Context context, @NonNull ConnectivityEventData data, boolean retriggerable, boolean persistent) {
        return new ConnectivitySlot(context, data, retriggerable, persistent);
    }

    @Override
    public Tracker<ConnectivityEventData> tracker(@NonNull Context context, @NonNull ConnectivityEventData data, @NonNull PendingIntent event_positive, @NonNull PendingIntent event_negative) {
        return new ConnectivityTracker(context, data, event_positive, event_negative);
    }

    @Deprecated
    @Override
    public EventSkill<ConnectivityEventData> event() {
        return ((USourceSkill<ConnectivityEventData>) this).event();
    }

}
