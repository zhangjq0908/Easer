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

package ryey.easer.commons.plugindef.eventplugin;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

/*
 * Slots are used to tell the current state of the relevant Event plugin.
 * Slots also carry the duty to receive the relevant signals (e.g. Intent broadcasts of Android) and notify the holder to proceed to check.
 *
 * Slots are used in `Lotus`.
 */
public abstract class AbstractSlot {
    /*
     * AndroidStudio reminds me that some fields and/or methods can be made private.
     * I'm not sure if they will be used by the subclasses in the future when extending the `satisfied` field to more status, so they are left as protected.
     */

    protected Context context;

    /*
     * Indicator of whether the current slot is satisfied.
     * Will be extended to more status (maybe by enum) in the future.
     */
    protected boolean satisfied = false;

    /*
     * Used to tell the holder Lotus that this Slot is satisfied.
     * Only the to-level (in the `EventTree`) slot will need this (to tell the Lotus to check the whole tree).
     */
    protected PendingIntent notifyLotusIntent = null, notifyLotusUnsatisfiedIntent = null;

    public AbstractSlot(Context context) {
        this.context = context;
    }

    /*
     * Report the current status of this slot.
     * Status will be extended to more (than boolean), so the return type will change in the future.
     */
    public boolean isSatisfied() {
        return satisfied;
    }

    /*
     * Set the trigger to be ready to receive the relevant Event.
     * The trigger will start functioning after `listen()` is called.
     */
    public abstract void set(EventData data);

    /*
     * Not sure if this methods is really needed.
     * Currently there is nothing using it.
     */
    public boolean isValid() {
        return true;
    }

    /*
     * Start functioning as a top-level listener.
     * When (probably satisfying) event happens, notify itself and finally proceed to `changeSatisfiedState()`
     *
     * Can be called multiply times (data remain the same).
     */
    public abstract void listen();

    /*
     * Stop functioning as a top-level listener.
     * No longer notify itself even when it is really satisfied.
     *
     * This methods doesn't prevent `check()` to set itself satisfied.
     */
    public abstract void cancel();

    /*
     * Check to see if the current slot is satisfied.
     * Should set `satisfied` and call `changeSatisfiedState()`.
     */
    public abstract void check();

    /*
     * Whether the Lotus can promote the sub events to a upper one (to listen to events).
     * Ideally:
     *   for continuous event types, it is the same as isSatistied();
     *   for temporary event types, it is always false.
     */
    public boolean canPromoteSub() {
        return isSatisfied();
    }

    /*
     * Set where to notify (the holder Lotus).
     */
    public void register(PendingIntent notifyLotusIntent, PendingIntent notifyLotusUnsatisfiedIntent) {
        this.notifyLotusIntent = notifyLotusIntent;
        this.notifyLotusUnsatisfiedIntent = notifyLotusUnsatisfiedIntent;
    }

    protected void changeSatisfiedState(boolean newSatisfiedState) {
        Log.d(getClass().getSimpleName(), String.format("changeSatisfiedState %s", newSatisfiedState));
        if (satisfied == newSatisfiedState) {
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
            Log.wtf(getClass().getSimpleName(), "slot not properly registered");
//            throw new RuntimeException("slot never registered");
        } else {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }
}
