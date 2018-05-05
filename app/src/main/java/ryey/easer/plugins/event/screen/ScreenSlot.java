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

package ryey.easer.plugins.event.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;

public class ScreenSlot extends AbstractSlot<ScreenEventData> {

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                changeSatisfiedState(eventData.screenEvent == ScreenEventData.ScreenEvent.on);
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                changeSatisfiedState(eventData.screenEvent == ScreenEventData.ScreenEvent.off);
            }
        }
    };

    IntentFilter intentFilter;

    ScreenSlot(Context context, ScreenEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    ScreenSlot(Context context, ScreenEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
    }

    @Override
    public void listen() {
        context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void cancel() {
        context.registerReceiver(mReceiver, intentFilter);
    }

    @Deprecated
    @Override
    public void check() {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm == null) {
            Logger.e("ScreenSlot can't get PowerManager");
            return;
        }
        if (pm.isScreenOn()) {
            changeSatisfiedState(eventData.screenEvent == ScreenEventData.ScreenEvent.on);
        } else {
            changeSatisfiedState(eventData.screenEvent == ScreenEventData.ScreenEvent.off);
        }
    }
}
