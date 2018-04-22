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

package ryey.easer.plugins.event.broadcast;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;

public class BroadcastEventPlugin implements EventPlugin<BroadcastEventData> {

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

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return true;
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
    public PluginViewFragment<BroadcastEventData> view() {
        return new BroadcastPluginViewFragment();
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
