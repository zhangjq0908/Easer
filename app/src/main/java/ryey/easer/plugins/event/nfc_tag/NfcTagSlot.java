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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.annotation.NonNull;

import java.util.Arrays;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

import static ryey.easer.commons.plugindef.eventplugin.EventType.is;

public class NfcTagSlot extends AbstractSlot {
    private NfcTagEventData data = null;
    private EventType type = null;

    private final BroadcastReceiver connReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] tag_id = tag.getId();
                if (Arrays.equals(tag_id, (byte[]) data.get())) {
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
    };

    private final IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

    {
        setRetriggerable(true);
    }

    public NfcTagSlot(Context context) {
        super(context);
    }

    @Override
    public void set(@NonNull EventData data) {
        if (data instanceof NfcTagEventData) {
            this.data = (NfcTagEventData) data;
            type = data.type();
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public void listen() {
        context.registerReceiver(connReceiver, filter);
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(connReceiver);
    }

    @Override
    public void check() {
    }
}
