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

package ryey.easer.plugins.condition.day_of_week;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.plugins.condition.SelfNotifiableSkeletonTracker;

public class DayOfWeekTracker extends SelfNotifiableSkeletonTracker<DayOfWeekConditionData> {
    private static AlarmManager mAlarmManager;

    DayOfWeekTracker(Context context, DayOfWeekConditionData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (data.days.contains(dayOfWeek - 1))
            newSatisfiedState(true);
        else
            newSatisfiedState(false);
    }

    private void scheduleAlarm() { //FIXME: not sure if this method is correct
        if (data.days.size() == 7) // Everyday
            return;

        PendingIntent pendingIntent = state() ? notifySelfIntent_negative : notifySelfIntent_positive;

        Calendar calendar = Calendar.getInstance();
        int next_day_of_week = calendar.get(Calendar.DAY_OF_WEEK) % 7;
        calendar.roll(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        while (data.days.contains(next_day_of_week)) {
            next_day_of_week = (next_day_of_week + 1) % 7;
            calendar.roll(Calendar.DAY_OF_YEAR, 1);
        }

        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                0, pendingIntent);
    }

    private void scheduleAllAlarms() {
        //TODO: optimise
        Calendar calendar = Calendar.getInstance();

        // Set calendar to 00:00 of today
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        for (int i = 0; i < 7; i++) {
            calendar.roll(Calendar.DAY_OF_YEAR, 1);
            if (calendar.get(Calendar.DAY_OF_YEAR) == 0) {
                calendar.roll(Calendar.YEAR, 1);
            }
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) % 7 - 1;
            PendingIntent pendingIntent;
            pendingIntent = data.days.contains(dayOfWeek) ? notifySelfIntent_positive : notifySelfIntent_negative;
            mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    @Override
    public void start() {
        scheduleAllAlarms();
    }

    @Override
    public void stop() {
        mAlarmManager.cancel(notifySelfIntent_positive);
        mAlarmManager.cancel(notifySelfIntent_negative);
    }

}
