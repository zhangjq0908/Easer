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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;
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

    // Fields used in relevant Intent
    protected static final String ACTION_SATISFIED = "ryey.easer.triggerlotus.abstractslot.SATISFIED";
    protected static final String CATEGORY_NOTIFY_SLOT = "ryey.easer.triggetlotus.category.NOTIFY_SLOT";

    /*
     * Indicator of whether the current slot is satisfied.
     * Will be extended to more status (maybe by enum) in the future.
     */
    protected boolean satisfied;

    /*
     * Mechanisms and fields used to notify the slot itself, and then proceed to `onSatisfied()`.
     * This is because some system-level checking mechanisms (e.g. data/time) need a PendingIntent.
     */
    protected Uri uri = Uri.parse(String.format("slot://%s/%d", getClass().getSimpleName(), hashCode()));
    protected PendingIntent notifySelfIntent;
    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SATISFIED))
                onSatisfied();
        }
    };

    /*
     * Used to tell the holder Lotus that this Slot is satisfied.
     * Only the to-level (in the `EventTree`) slot will need this (to tell the Lotus to check the whole tree).
     */
    protected PendingIntent notifyLotusIntent = null;

    public AbstractSlot(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SATISFIED);
        filter.addCategory(CATEGORY_NOTIFY_SLOT);
        filter.addDataScheme(uri.getScheme());
        filter.addDataAuthority(uri.getAuthority(), null);
        filter.addDataPath(uri.getPath(), PatternMatcher.PATTERN_LITERAL);
        context.registerReceiver(mReceiver, filter);

        Intent intent = new Intent(ACTION_SATISFIED);
        intent.addCategory(CATEGORY_NOTIFY_SLOT);
        intent.setData(uri);
        notifySelfIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
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
     * The trigger will start functioning after `apply()` is called.
     */
    public abstract void set(EventData data);

    /*
     * Not sure if this methods is really needed.
     * Currently there is nothing using it.
     */
    public abstract boolean isValid();

    /*
     * Start functioning as a top-level listener.
     * When satisfied, notify itself (to proceed to `onSatisfied()`)
     *
     * Can be called multiply times (data remain the same).
     */
    public abstract void apply();

    /*
     * Stop functioning as a top-level listener.
     * No longer notify itself even when it is really satisfied.
     *
     * This methods doesn't prevent `check()` to set itself satisfied.
     */
    public abstract void cancel();

    /*
     * Check to see if the current slot is satisfied.
     * Will set `satisfied` internally (by calling `justSatisfied()`)
     */
    public abstract void check();

    /*
     * Set where to notify (the holder Lotus).
     */
    public void register(PendingIntent notifyLotusIntent) {
        this.notifyLotusIntent = notifyLotusIntent;
    }

    protected void changeSatisfiedState(boolean newSatisfiedState) {
        Log.d(getClass().getSimpleName(), String.format("changeSatisfiedState %s", newSatisfiedState));
        satisfied = newSatisfiedState;
        if (newSatisfiedState) {
            try {
                notifySelfIntent.send();
            } catch (PendingIntent.CanceledException e) {
                //TODO: shouldn't happen because this method is only called in subclasses
                e.printStackTrace();
                throw new IllegalAccessError();
            }
        }
    }

    void onSatisfied() {
        Log.d(getClass().getSimpleName(), "onSatisfied");
        satisfied = true;
        if (notifyLotusIntent == null) {
//            Log.wtf(getClass().getSimpleName(), "slot never registered");
//            throw new RuntimeException("slot never registered");
            Log.d(getClass().getSimpleName(), "slot not registered. probably because is in the recursive tree");
        } else {
            try {
                notifyLotusIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }
}
