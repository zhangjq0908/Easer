/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.event.sms;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.plugins.reusable.PluginHelper;

public class SmsEventPlugin implements EventPlugin {

    @NonNull
    @Override
    public String id() {
        return "sms";
    }

    @Override
    public int name() {
        return R.string.event_sms;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return PluginHelper.checkPermission(context,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        boolean can_read_sms = PluginHelper.checkPermission(activity, Manifest.permission.READ_SMS);
        boolean can_receive_sms = PluginHelper.checkPermission(activity, Manifest.permission.RECEIVE_SMS);
        if (!can_read_sms && !can_receive_sms) {
            PluginHelper.requestPermission(activity, requestCode,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS);
        } else if (!can_read_sms) {
            PluginHelper.requestPermission(activity, requestCode, Manifest.permission.READ_SMS);
        } else {
            PluginHelper.requestPermission(activity, requestCode, Manifest.permission.RECEIVE_SMS);
        }
    }

    @NonNull
    @Override
    public EventData data() {
        return new SmsEventData();
    }

    @NonNull
    @Override
    public PluginViewFragment view() {
        return new SmsPluginViewFragment();
    }

    @Override
    public AbstractSlot slot(Context context) {
        return new SmsConnSlot(context);
    }
}
