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

package ryey.easer.commons.plugindef.eventplugin;

import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.plugindef.ValidData;

/**
 * Slots are used to tell the current state of the relevant Event plugin.
 * Slots also carry the duty to receive the relevant signals (e.g. Intent broadcasts of Android) and notify the holder to proceed to check.
 *
 * Slots are used in {@link ryey.easer.core.Lotus}.
 */
public abstract class AbstractSlot<T extends EventData> {
    /**
     * AndroidStudio reminds me that some fields and/or methods can be made private.
     * I'm not sure if they will be used by the subclasses in the future when extending the `satisfied` field to more status, so they are left as protected.
     */

    protected final Context context;

    protected final T eventData;

    /**
     * Indicator of whether the current slot is satisfied.
     * May be extended to more status (maybe by enum) in the future.
     */
    protected boolean satisfied = false;

    /**
     * Controls whether the current slot could be re-triggered if it is already in a satisfied state.
     * If uncertain, make it re-triggerable
     */
    protected final boolean retriggerable;
    protected final boolean persistent;

    protected static final boolean RETRIGGERABLE_DEFAULT = true;
    protected static final boolean PERSISTENT_DEFAULT = false;

    /**
     * Used to tell the holder Lotus that this Slot is satisfied.
     * Only the to-level (in the {@link ryey.easer.core.data.EventTree}) slot will need this (to tell the {@link ryey.easer.core.Lotus} to check the whole tree).
     */
    protected PendingIntent notifyLotusIntent = null, notifyLotusUnsatisfiedIntent = null;

    public AbstractSlot(@NonNull Context context, @ValidData @NonNull T data, boolean retriggerable, boolean persistent) {
        this.context = context;
        this.eventData = data;
        this.retriggerable = retriggerable;
        this.persistent = persistent;
    }

    /**
     * Start functioning as a top-level listener.
     * When (probably satisfying) event happens, notify itself and finally proceed to {@link #changeSatisfiedState(boolean)}.
     *
     * Can be called multiply times (data remain the same).
     */
    public abstract void listen();

    /**
     * Stop functioning as a top-level listener.
     * No longer notify itself even when it is really satisfied.
     *
     * This methods doesn't prevent {@link #check()} to set itself satisfied.
     */
    public abstract void cancel();

    /**
     * Check to see if the current slot is satisfied.
     * Should set {@link #satisfied} by calling {@link #changeSatisfiedState(boolean)}.
     */
    public abstract void check();

    /**
     * Set where to notify (the holder {@link ryey.easer.core.Lotus}).
     */
    public void register(@NonNull PendingIntent notifyLotusIntent, @NonNull PendingIntent notifyLotusUnsatisfiedIntent) {
        this.notifyLotusIntent = notifyLotusIntent;
        this.notifyLotusUnsatisfiedIntent = notifyLotusUnsatisfiedIntent;
    }

    /**
     * Change the satisfaction state of the current slot.
     * It will emit {@link #notifyLotusIntent} or {@link #notifyLotusUnsatisfiedIntent} iif the satisfaction state is changed.
     *
     * This method sets the {@link #satisfied} variable.
     */
    protected synchronized void changeSatisfiedState(boolean newSatisfiedState) {
        if (persistent && satisfied && !newSatisfiedState) {
            Logger.v("prevent from resetting a persistent slot back to unsatisfied");
            return;
        }
        if (!retriggerable && (satisfied == newSatisfiedState)) {
            Logger.v("satisfied state is already %s", newSatisfiedState);
            return;
        }
        satisfied = newSatisfiedState;
        PendingIntent pendingIntent;
        if (satisfied) {
            pendingIntent = notifyLotusIntent;
        } else {
            pendingIntent = notifyLotusUnsatisfiedIntent;
        }
        if (pendingIntent == null) {
            Logger.w("slot not properly registered");
//            throw new RuntimeException("slot never registered");
        } else {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Logger.wtf("PendingIntent shouldn't be cancelled");
                e.printStackTrace();
            }
        }
        Logger.d("finished changeSatisfiedState to %s", newSatisfiedState);
    }
}
