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

package ryey.easer.plugins.condition.calendar;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.plugins.PluginViewFragment;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionDataFactory;
import ryey.easer.commons.local_plugin.conditionplugin.ConditionPlugin;
import ryey.easer.commons.local_plugin.conditionplugin.Tracker;
import ryey.easer.plugins.reusable.PluginHelper;

public class CalendarConditionPlugin implements ConditionPlugin<CalendarConditionData> {

    @NonNull
    @Override
    public String id() {
        return "calendar_condition";
    }

    @Override
    public int name() { return R.string.event_calendar; }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return PluginHelper.checkPermission(context, Manifest.permission.READ_CALENDAR);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        PluginHelper.requestPermission(activity, requestCode, Manifest.permission.READ_CALENDAR);
    }

    @NonNull
    @Override
    public ConditionDataFactory<CalendarConditionData> dataFactory() {
        return new CalendarConditionDataFactory();
    }

    @NonNull
    @Override
    public PluginViewFragment<CalendarConditionData> view() {
        return new CalendarPluginViewFragment();
    }

    @NonNull
    @Override
    public Tracker<CalendarConditionData> tracker(@NonNull Context context,
                                                 @ValidData @NonNull CalendarConditionData data,
                                                 @NonNull PendingIntent event_positive,
                                                 @NonNull PendingIntent event_negative) {
        return new CalendarTracker(context, data, event_positive, event_negative);
    }

}
