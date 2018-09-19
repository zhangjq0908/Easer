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

package ryey.easer.plugins.condition;

import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ryey.easer.commons.local_plugin.conditionplugin.ConditionData;
import ryey.easer.commons.local_plugin.conditionplugin.Tracker;

public abstract class SkeletonTracker<D extends ConditionData> implements Tracker<D> {

    protected final Context context;
    protected final D data;
    protected final PendingIntent event_positive, event_negative;

    Lock lck_satisfied = new ReentrantLock();
    protected Boolean satisfied;

    protected SkeletonTracker(Context context, D data,
                              @NonNull PendingIntent event_positive,
                              @NonNull PendingIntent event_negative) {
        this.context = context;
        this.data = data;
        this.event_positive = event_positive;
        this.event_negative = event_negative;
    }

    protected final void newSatisfiedState(Boolean newState) {
        lck_satisfied.lock();
        try {
            if (satisfied == newState) {
                return;
            }
            satisfied = newState;
            if (satisfied == null)
                return;
            PendingIntent pendingIntent = satisfied ? event_positive : event_negative;
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Logger.wtf("PendingIntent for notify in SkeletonTracker cancelled before sending???");
                e.printStackTrace();
            }
        } finally {
            lck_satisfied.unlock();
        }
    }

    @Override
    public Boolean state() {
        return satisfied;
    }
}
