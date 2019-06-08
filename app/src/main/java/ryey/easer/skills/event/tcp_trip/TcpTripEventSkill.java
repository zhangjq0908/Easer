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

package ryey.easer.skills.event.tcp_trip;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.eventskill.EventDataFactory;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.skills.event.AbstractSlot;

public class TcpTripEventSkill implements EventSkill<TcpTripEventData> {

    @NonNull
    @Override
    public String id() {
        return "tcp trip";
    }

    @Override
    public int name() {
        return R.string.event_tcp_trip;
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
    public EventDataFactory<TcpTripEventData> dataFactory() {
        return new TcpTripEventDataFactory();

    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.data_communication;
    }

    @NonNull
    @Override
    public SkillView<TcpTripEventData> view() {
        return new TcpTripSkillViewFragment();
    }

    @Override
    public AbstractSlot<TcpTripEventData> slot(@NonNull Context context, @ValidData @NonNull TcpTripEventData data) {
        return new TcpTripSlot(context, data);
    }

    @Override
    public AbstractSlot<TcpTripEventData> slot(@NonNull Context context, @NonNull TcpTripEventData data, boolean retriggerable, boolean persistent) {
        return new TcpTripSlot(context, data, retriggerable, persistent);
    }

}
