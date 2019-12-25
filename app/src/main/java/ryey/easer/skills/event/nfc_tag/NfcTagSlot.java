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

package ryey.easer.skills.event.nfc_tag;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;

import com.google.common.io.BaseEncoding;

import java.util.Arrays;

import ryey.easer.skills.event.AbstractSlot;

public class NfcTagSlot extends AbstractSlot<NfcTagEventData> {

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

    public NfcTagSlot(Context context, NfcTagEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    NfcTagSlot(Context context, NfcTagEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        context.bindService(sIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void cancel() {
        context.unbindService(mConnection);
    }

    void checkAndTrigger(final Tag tag) {
        byte[] tag_id = tag.getId();
        if (Arrays.equals(tag_id, eventData.id)) {
            Bundle dynamics = new Bundle();
            dynamics.putString(NfcTagEventData.NfcTagIdDynamics.id, BaseEncoding.base16().lowerCase().encode(tag_id));
            changeSatisfiedState(true, dynamics);
        } else {
            changeSatisfiedState(false);
        }
    }
}
