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

package ryey.easer.skills.usource.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import ryey.easer.skills.event.AbstractSlot;

public class ScreenSlot extends AbstractSlot<ScreenUSourceData> {

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                changeSatisfiedState(eventData.screenEvent == ScreenUSourceData.ScreenEvent.on);
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                changeSatisfiedState(eventData.screenEvent == ScreenUSourceData.ScreenEvent.off);
            } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                changeSatisfiedState(eventData.screenEvent == ScreenUSourceData.ScreenEvent.unlocked);
            }
        }
    };

    IntentFilter intentFilter;

    ScreenSlot(Context context, ScreenUSourceData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    ScreenSlot(Context context, ScreenUSourceData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void cancel() {
        context.registerReceiver(mReceiver, intentFilter);
    }

}
