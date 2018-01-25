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

package ryey.easer.plugins.event.nfc_tag;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.plugins.reusable.PluginHelper;

public class NfcTagEventPlugin implements EventPlugin<NfcTagEventData> {

    @NonNull
    @Override
    public String id() {
        return "nfc_tag";
    }

    @Override
    public int name() {
        return R.string.event_nfc_tag;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        return adapter != null;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return PluginHelper.checkPermission(context,
                Manifest.permission.NFC);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        PluginHelper.requestPermission(activity, requestCode, Manifest.permission.NFC);
    }

    @NonNull
    @Override
    public EventDataFactory<NfcTagEventData> dataFactory() {
        return new NfcTagEventDataFactory();

    }

    @NonNull
    @Override
    public PluginViewFragment<NfcTagEventData> view() {
        return new NfcTagPluginViewFragment();
    }

    @Override
    public AbstractSlot<NfcTagEventData> slot(@NonNull Context context, @ValidData @NonNull NfcTagEventData data) {
        return new NfcTagSlot(context, data);
    }

}
