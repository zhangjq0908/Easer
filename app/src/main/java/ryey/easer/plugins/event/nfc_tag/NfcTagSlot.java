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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.IBinder;
import android.support.annotation.NonNull;

import java.util.Arrays;

import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventType;

import static ryey.easer.commons.plugindef.eventplugin.EventType.is;

public class NfcTagSlot extends AbstractSlot<NfcTagEventData> {
    private NfcTagEventData data = null;
    private EventType type = null;

    private NfcListenerService.NLSBinder sBinder;

    private final Intent sIntent = new Intent(context, NfcListenerService.class);
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            sBinder = (NfcListenerService.NLSBinder) iBinder;
            sBinder.registerSlot(NfcTagSlot.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            sBinder.unregisterSlot(NfcTagSlot.this);
            sBinder = null;
        }
    };

    {
        setRetriggerable(true);
    }

    public NfcTagSlot(Context context) {
        super(context);
    }

    @Override
    public void set(@ValidData @NonNull NfcTagEventData data) {
        this.data = data;
        type = data.type();
    }

    @Override
    public void listen() {
        context.bindService(sIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void cancel() {
        context.unbindService(mConnection);
    }

    @Override
    public void check() {
    }

    void checkAndTrigger(final Tag tag) {
        byte[] tag_id = tag.getId();
        if (Arrays.equals(tag_id, data.id)) {
            if (type == is)
                changeSatisfiedState(true);
            else
                changeSatisfiedState(false);
        } else {
            if (type == is)
                changeSatisfiedState(false);
            else
                changeSatisfiedState(true);
        }
    }
}
