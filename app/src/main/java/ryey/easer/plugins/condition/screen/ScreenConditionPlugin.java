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

package ryey.easer.plugins.condition.screen;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.conditionplugin.ConditionDataFactory;
import ryey.easer.commons.plugindef.conditionplugin.ConditionPlugin;
import ryey.easer.commons.plugindef.conditionplugin.Tracker;

public class ScreenConditionPlugin implements ConditionPlugin<ScreenConditionData> {

    @NonNull
    @Override
    public String id() {
        return "screen";
    }

    @Override
    public int name() {
        return R.string.condition_screen;
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
    public ConditionDataFactory<ScreenConditionData> dataFactory() {
        return new ScreenConditionDataFactory();
    }

    @NonNull
    @Override
    public PluginViewFragment<ScreenConditionData> view() {
        return new ScreenPluginViewFragment();
    }

    @NonNull
    @Override
    public Tracker<ScreenConditionData> tracker(@NonNull Context context,
                                                 @ValidData @NonNull ScreenConditionData data,
                                                 @NonNull PendingIntent event_positive,
                                                 @NonNull PendingIntent event_negative) {
        return new ScreenTracker(context, data, event_positive, event_negative);
    }

}
