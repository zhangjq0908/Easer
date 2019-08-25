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

package ryey.easer.skills.usource.day_of_week;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.Set;

import ryey.easer.skills.event.SelfNotifiableSlot;

class DayOfWeekSlot extends SelfNotifiableSlot<DayOfWeekUSourceData> {
    private static AlarmManager mAlarmManager;

    @NonNull
    private final Set<Integer> days;

    DayOfWeekSlot(Context context, DayOfWeekUSourceData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    DayOfWeekSlot(Context context, DayOfWeekUSourceData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        this.days = data.days;

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void listen() {
        super.listen();
        Utils.scheduleAlarmEveryday(mAlarmManager, notifySelfIntent_positive);
    }

    @Override
    public void cancel() {
        super.cancel();
        mAlarmManager.cancel(notifySelfIntent_positive);
    }

    @Override
    protected void onPositiveNotified(Intent intent) {
        if (Utils.isSatisfied(days)) {
            changeSatisfiedState(true);
        } else {
            changeSatisfiedState(false);
        }
    }

}
