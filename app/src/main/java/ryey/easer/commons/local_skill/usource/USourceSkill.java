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

package ryey.easer.commons.local_skill.usource;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.conditionskill.ConditionDataFactory;
import ryey.easer.commons.local_skill.conditionskill.ConditionSkill;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.commons.local_skill.eventskill.EventDataFactory;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.commons.local_skill.eventskill.Slot;

public interface USourceSkill<D extends USourceData> extends Skill<D> {

    String EXTRA_INFO_TYPE = "ryey.easer.skill.source.type";
    String INFO_TYPE_EVENT = "event";
    String INFO_TYPE_CONDITION = "condition";

    @NonNull
    USourceDataFactory<D> dataFactory();

    @NonNull
    default SourceCategory category() {
        return SourceCategory.unknown;
    }

    /**
     * Default value for eventView() and conditionView().
     * Returns the control UI of this plugin.
     * Used in relevant UI.
     */
    @NonNull
    SkillView<D> view();

    Slot<D> slot(@NonNull Context context, @NonNull D data);

    Slot<D> slot(@NonNull Context context, @NonNull D data, boolean retriggerable, boolean persistent);

    Tracker<D> tracker(@NonNull Context context, @NonNull D data, @NonNull PendingIntent event_positive, @NonNull PendingIntent event_negative);

    default SkillView<D> eventView() {
        Bundle args = new Bundle();
        args.putString(EXTRA_INFO_TYPE, INFO_TYPE_EVENT);
        SkillView<D> v = view();
        v.setArguments(args);
        return v;
    }

    default SkillView<D> conditionView() {
        Bundle args = new Bundle();
        args.putString(EXTRA_INFO_TYPE, INFO_TYPE_CONDITION);
        SkillView<D> v = view();
        v.setArguments(args);
        return v;
    }

    default EventSkill<D> event() {
        return new EventSkill<D>() {
            @NonNull
            @Override
            public EventDataFactory<D> dataFactory() {
                return USourceSkill.this.dataFactory();
            }

            @NonNull
            @Override
            public SourceCategory category() {
                return USourceSkill.this.category();
            }

            @Override
            public Slot<D> slot(@NonNull Context context, @NonNull D data) {
                return USourceSkill.this.slot(context, data);
            }

            @Override
            public Slot<D> slot(@NonNull Context context, @NonNull D data, boolean retriggerable, boolean persistent) {
                return USourceSkill.this.slot(context, data, retriggerable, persistent);
            }

            @NonNull
            @Override
            public String id() {
                return USourceSkill.this.id();
            }

            @Override
            public int name() {
                return USourceSkill.this.name();
            }

            @Override
            public boolean isCompatible(@NonNull Context context) {
                return USourceSkill.this.isCompatible(context);
            }

            @Override
            public boolean checkPermissions(@NonNull Context context) {
                return USourceSkill.this.checkPermissions(context);
            }

            @Override
            public void requestPermissions(@NonNull Activity activity, int requestCode) {
                USourceSkill.this.requestPermissions(activity, requestCode);
            }

            @NonNull
            @Override
            public SkillView<D> view() {
                return USourceSkill.this.eventView();
            }
        };
    }

    default ConditionSkill<D> condition() {
        return new ConditionSkill<D>() {
            @NonNull
            @Override
            public ConditionDataFactory<D> dataFactory() {
                return USourceSkill.this.dataFactory();
            }

            @NonNull
            @Override
            public SourceCategory category() {
                return USourceSkill.this.category();
            }

            @NonNull
            @Override
            public Tracker<D> tracker(@NonNull Context context, @NonNull D data, @NonNull PendingIntent event_positive, @NonNull PendingIntent event_negative) {
                return USourceSkill.this.tracker(context, data, event_positive, event_negative);
            }

            @NonNull
            @Override
            public String id() {
                return USourceSkill.this.id();
            }

            @Override
            public int name() {
                return USourceSkill.this.name();
            }

            @Override
            public boolean isCompatible(@NonNull Context context) {
                return USourceSkill.this.isCompatible(context);
            }

            @Override
            public boolean checkPermissions(@NonNull Context context) {
                return USourceSkill.this.checkPermissions(context);
            }

            @Override
            public void requestPermissions(@NonNull Activity activity, int requestCode) {
                USourceSkill.this.requestPermissions(activity, requestCode);
            }

            @NonNull
            @Override
            public SkillView<D> view() {
                return USourceSkill.this.conditionView();
            }
        };
    }

}
