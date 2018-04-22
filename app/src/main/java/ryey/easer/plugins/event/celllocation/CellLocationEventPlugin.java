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

package ryey.easer.plugins.event.celllocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.plugins.reusable.PluginHelper;

public class CellLocationEventPlugin implements EventPlugin<CellLocationEventData> {

    @NonNull
    @Override
    public String id() {
        return "cell location";
    }

    @Override
    public int name() {
        return R.string.event_celllocation;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return PluginHelper.checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        PluginHelper.requestPermission(activity, requestCode, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @NonNull
    @Override
    public EventDataFactory<CellLocationEventData> dataFactory() {
        return new CellLocationEventDataFactory();
    }

    @NonNull
    @Override
    public PluginViewFragment<CellLocationEventData> view() {
        return new CellLocationPluginViewFragment();
    }

    @Override
    public AbstractSlot<CellLocationEventData> slot(@NonNull Context context, @ValidData @NonNull CellLocationEventData data) {
        return new CellLocationSlot(context, data);
    }

    @Override
    public AbstractSlot<CellLocationEventData> slot(@NonNull Context context, @NonNull CellLocationEventData data, boolean retriggerable, boolean persistent) {
        return new CellLocationSlot(context, data, retriggerable, persistent);
    }

}
