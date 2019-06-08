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

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;

import androidx.collection.ArraySet;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Set;

public class NfcListenerService extends Service {
    static final String ACTION_NFC_SCANNED = "ryey.easer.skills.event.nfc_tag.action.NFC_SCANNED";

    private final IntentFilter mFilter = new IntentFilter(ACTION_NFC_SCANNED);
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_NFC_SCANNED.equals(intent.getAction())) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                for (NfcTagSlot slot : registeredSlots) {
                    slot.checkAndTrigger(tag);
                }
            }
        }
    };

    private Set<NfcTagSlot> registeredSlots = new ArraySet<>();

    public NfcListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new NLSBinder();
    }

    class NLSBinder extends Binder {
        void registerSlot(NfcTagSlot slot) {
            registeredSlots.add(slot);
        }

        void unregisterSlot(NfcTagSlot slot) {
            registeredSlots.remove(slot);
        }
    }
}
