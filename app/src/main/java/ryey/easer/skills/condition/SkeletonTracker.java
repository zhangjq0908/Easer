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
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ryey.easer.commons.local_skill.conditionskill.ConditionData;
import ryey.easer.commons.local_skill.conditionskill.Tracker;

/**
 * A skeleton implementation for {@link Tracker<D>}.
 * This should be the starting point for most (if not all) implementations.
 * It contains the basic functionalities, including keeping track of states, and notifying the
 * upstream (through sending out {@link #event_positive} and {@link #event_negative}).
 * It should be concurrency safe.
 *
 * You may also be interested in {@link SelfNotifiableSkeletonTracker} for certain uses.
 *
 * @param <D> The data class for the relevant condition.
 */
public abstract class SkeletonTracker<D extends ConditionData> implements Tracker<D> {

    protected final Context context;
    protected final D data;
    protected final PendingIntent event_positive, event_negative;

    Lock lck_satisfied = new ReentrantLock();
    protected Boolean satisfied = null;  // The current state; null represents unknown.

    /**
     * Initializes an Tracker object for the current condition.
     * Implementors should also set the initial states (if any) in the constructor. Use {@link #newSatisfiedState(Boolean)} if so.
     * @param context Context
     * @param data The data to be used/checked in the condition
     * @param event_positive The intent to send (to the upstream) when the condition becomes true
     * @param event_negative The intent to send (to the upstream) when the condition becomes false
     */
    protected SkeletonTracker(Context context, D data,
                              @NonNull PendingIntent event_positive,
                              @NonNull PendingIntent event_negative) {
        this.context = context;
        this.data = data;
        this.event_positive = event_positive;
        this.event_negative = event_negative;
    }

    /**
     * Call this method to notify about the (potentially changed) current state.
     * It sends out the relevant intents so the upstream gets informed.
     * This method checks if the state is really different. It will not do anything if so.
     * @param newState The (potentially changed) newly-informed state.
     */
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

    /**
     * Generally the subclasses do not need to override this method.
     * To set initial states, do it in the constructor.
     * @return The current state, or {@code null} representing unknown.
     */
    @Nullable
    @Override
    public Boolean state() {
        return satisfied;
    }
}
