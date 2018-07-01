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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.core.Lotus;
import ryey.easer.core.data.ScriptTree;

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
    protected Boolean satisfied = null;

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
     * Only the to-level (in the {@link ScriptTree}) slot will need this (to tell the {@link ryey.easer.core.Lotus} to check the whole tree).
     */
    protected Uri notifyLotusData;

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
     * @deprecated See <a href="https://github.com/renyuneyun/Easer/issues/82">this proposal</a>. New implementations *can* still define this, but will be removed in the future.
     */
    @Deprecated
    public abstract void check();

    /**
     * Set where to notify (the holder {@link ryey.easer.core.Lotus}).
     */
    public void register(Uri data) {
        this.notifyLotusData = data;
    }

    /**
     * Change the satisfaction state of the current slot.
     * It will notify the corresponding {@link ryey.easer.core.Lotus} (by emitting {@link android.content.Intent} with {@link #notifyLotusData} as data) iif the satisfaction state is changed.
     *
     * This method sets the {@link #satisfied} variable.
     */
    protected synchronized void changeSatisfiedState(boolean newSatisfiedState) {
        if (satisfied != null) {
            if (persistent && satisfied && !newSatisfiedState) {
                Logger.v("prevent from resetting a persistent slot back to unsatisfied");
                return;
            }
            if (!retriggerable && (satisfied == newSatisfiedState)) {
                Logger.v("satisfied state is already %s", newSatisfiedState);
                return;
            }
        }
        satisfied = newSatisfiedState;
        //FIXME: remove the explicit use of core package (Lotus)
        Intent notifyLotusIntent = satisfied
                ? Lotus.NotifyIntentPrototype.obtainPositiveIntent(notifyLotusData)
                : Lotus.NotifyIntentPrototype.obtainNegativeIntent(notifyLotusData);
        context.sendBroadcast(notifyLotusIntent);
        Logger.d("finished changeSatisfiedState to %s", newSatisfiedState);
    }
}
