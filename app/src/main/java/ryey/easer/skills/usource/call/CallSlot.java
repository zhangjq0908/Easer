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

package ryey.easer.skills.usource.call;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import ryey.easer.skills.event.AbstractSlot;

public class CallSlot extends AbstractSlot<CallUSourceData> implements CallReceiver.CallEventHandler {

    private CallReceiver receiver = new CallReceiver(this);

    public CallSlot(Context context, CallUSourceData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    CallSlot(Context context, CallUSourceData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        context.registerReceiver(receiver, CallReceiver.Companion.getCallFilter());
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(receiver);
    }

    @Override
    public void onIdle(@NotNull String number) {
        changeSatisfiedState(CallReceiver.Companion.handleIdle(eventData, number));
    }

    @Override
    public void onRinging(@NotNull String number) {
        changeSatisfiedState(CallReceiver.Companion.handleRinging(eventData, number));
    }

    @Override
    public void onOffHook(@NotNull String number) {
        changeSatisfiedState(CallReceiver.Companion.handleOffHook(eventData, number));
    }
}
