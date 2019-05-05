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

package ryey.easer.plugins.event.condition_event;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.PluginViewFragmentInterface;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.commons.local_plugin.eventplugin.EventDataFactory;
import ryey.easer.commons.local_plugin.eventplugin.EventPlugin;
import ryey.easer.plugins.event.AbstractSlot;

/**
 * This plugin belongs to core - not likely able to be split out
 */
public class ConditionEventEventPlugin implements EventPlugin<ConditionEventEventData> {

    @NonNull
    @Override
    public String id() {
        return "condition_event";
    }

    @Override
    public int name() {
        return R.string.ev_condition_event;
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
    public EventDataFactory<ConditionEventEventData> dataFactory() {
        return new ConditionEventEventDataFactory();
    }

    @NonNull
    @Override
    public PluginViewFragmentInterface<ConditionEventEventData> view() {
        return new ConditionEventPluginViewFragment();
    }

    @Override
    public AbstractSlot<ConditionEventEventData> slot(@NonNull Context context, @ValidData @NonNull ConditionEventEventData data) {
        return new ConditionEventSlot(context, data);
    }

    @Override
    public AbstractSlot<ConditionEventEventData> slot(@NonNull Context context, @NonNull ConditionEventEventData data, boolean retriggerable, boolean persistent) {
        return new ConditionEventSlot(context, data, retriggerable, persistent);
    }

}
