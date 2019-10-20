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

package ryey.easer.skills.condition;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.Locale;

import ryey.easer.commons.local_skill.conditionskill.ConditionData;

public abstract class SelfNotifiableSkeletonTracker<D extends ConditionData> extends SkeletonTracker<D> {
    // Fields used in relevant Intent
    private static final String ACTION_SATISFIED = "ryey.easer.tracker.SATISFIED";
    private static final String ACTION_UNSATISFIED = "ryey.easer.tracker.UNSATISFIED";
    private static final String CATEGORY_NOTIFY_SLOT = "ryey.easer.tracker.category.NOTIFY_SLOT";
    /*
     * Mechanisms and fields used to notify the slot itself, and then proceed to `onPositiveNotified()`.
     * This is because some system-level checking mechanisms (e.g. data/time) need a PendingIntent.
     */
    protected final Uri uri = Uri.parse(String.format(Locale.US, "slot://%s/%d", getClass().getSimpleName(), hashCode()));
    // After sent, this will trigger onPositiveNotified().
    // Meant to be used when the event is going to a positive state.
    protected final PendingIntent notifySelfIntent_positive;
    // After sent, this will trigger onNegativeNotified().
    // Meant to be used when the event is going to a negative state.
    protected final PendingIntent notifySelfIntent_negative;
    private final IntentFilter filter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d("self notifying Intent received. action: %s", intent.getAction());
            if (intent.getAction().equals(ACTION_SATISFIED)) {
                onPositiveNotified();
            } else if (intent.getAction().equals(ACTION_UNSATISFIED)) {
                onNegativeNotified();
            }
        }
    };

    {
        filter = new IntentFilter();
        filter.addAction(ACTION_SATISFIED);
        filter.addAction(ACTION_UNSATISFIED);
        filter.addCategory(CATEGORY_NOTIFY_SLOT);
        filter.addDataScheme(uri.getScheme());
        filter.addDataAuthority(uri.getAuthority(), null);
        filter.addDataPath(uri.getPath(), PatternMatcher.PATTERN_LITERAL);

        Intent intent = new Intent(ACTION_SATISFIED);
        intent.addCategory(CATEGORY_NOTIFY_SLOT);
        intent.setData(uri);
        notifySelfIntent_positive = PendingIntent.getBroadcast(context, 0, intent, 0);
        intent.setAction(ACTION_UNSATISFIED);
        notifySelfIntent_negative = PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    protected SelfNotifiableSkeletonTracker(Context context, D data, @NonNull PendingIntent event_positive, @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);
    }

    protected void onPositiveNotified() {
        Logger.v("onPositiveNotified");
        newSatisfiedState(true);
    }

    protected void onNegativeNotified() {
        Logger.v("onNegativeNotified");
        newSatisfiedState(false);
    }

    @Override
    @CallSuper
    public void start() {
        context.registerReceiver(mReceiver, filter);
    }

    @Override
    @CallSuper
    public void stop() {
        context.unregisterReceiver(mReceiver);
    }
}
