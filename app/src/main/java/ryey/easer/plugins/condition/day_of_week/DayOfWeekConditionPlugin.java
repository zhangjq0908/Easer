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

package ryey.easer.plugins.condition.day_of_week;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.PluginViewFragmentInterface;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionDataFactory;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionPlugin;
import ryey.easer.commons.local_plugin.conditionplugin.Tracker;

public class DayOfWeekConditionPlugin implements ConditionPlugin<DayOfWeekConditionData> {

    @NonNull
    @Override
    public String id() {
        return "day_of_week";
    }

    @Override
    public int name() {
        return R.string.condition_day_of_week;
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
    public ConditionDataFactory<DayOfWeekConditionData> dataFactory() {
        return new DayOfWeekConditionDataFactory();
    }

    @NonNull
    @Override
    public PluginViewFragmentInterface<DayOfWeekConditionData> view() {
        return new DayOfWeekPluginViewFragment();
    }

    @NonNull
    @Override
    public Tracker<DayOfWeekConditionData> tracker(@NonNull Context context,
                                                 @ValidData @NonNull DayOfWeekConditionData data,
                                                 @NonNull PendingIntent event_positive,
                                                 @NonNull PendingIntent event_negative) {
        return new DayOfWeekTracker(context, data, event_positive, event_negative);
    }

}
