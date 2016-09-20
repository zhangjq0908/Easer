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

package ryey.easer.commons;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;
import android.util.Log;

public abstract class AbstractSlot {
    protected static final String ACTION_SATISFIED = "ryey.easer.triggerlotus.abstractslot.SATISFIED";
    protected static final String CATEGORY_NOTIFY_SLOT = "ryey.easer.triggetlotus.category.NOTIFY_SLOT";

    protected boolean satisfied;

    protected Uri uri = Uri.parse(String.format("slot://%s/%d", getClass().getSimpleName(), hashCode()));
    protected PendingIntent notifySelfIntent; // May be used by some special system-level checking mechanisms (e.g. date / time)

    protected PendingIntent notifyLotusIntent = null;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SATISFIED))
                onSatisfied();
        }
    };

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

    public boolean isSatisfied() {
        return satisfied;
    }

    public abstract void set(EventData data);

    public abstract boolean isValid();

    public abstract void apply();

    public abstract void cancel();

    /*
     * Check to see if the current slot is satisfied.
     * Will set `satisfied` internally (by calling `justSatisfied()`)
     */
    public abstract void check();

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
