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

package ryey.easer.skills.event.time;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ryey.easer.skills.event.SelfNotifiableSlot;

public class TimeSlot extends SelfNotifiableSlot<TimeEventData> {
    private static AlarmManager mAlarmManager;

    private final Calendar calendar;

    public TimeSlot(Context context, TimeEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    TimeSlot(Context context, TimeEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);

        calendar = Calendar.getInstance();
        long currentTimeMillis = System.currentTimeMillis();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.set(Calendar.HOUR_OF_DAY, data.time.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, data.time.get(Calendar.MINUTE));
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTimeMillis);
        if (calendar.before(now)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void listen() {
        super.listen();
        if (calendar != null) {
            mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, notifySelfIntent_positive);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (calendar != null) {
            mAlarmManager.cancel(notifySelfIntent_positive);
            mAlarmManager.cancel(notifySelfIntent_negative);
        }
    }

    @Override
    protected void onPositiveNotified(Intent intent) {
        changeSatisfiedState(true);
    }

    @Override
    protected void onNegativeNotified(Intent intent) {
    }
}