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

package ryey.easer.skills.usource.time;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.skills.condition.SelfNotifiableSkeletonTracker;

public class TimeTracker extends SelfNotifiableSkeletonTracker<TimeUSourceData> {
    private static AlarmManager mAlarmManager;

    TimeTracker(Context context, TimeUSourceData data,
                @NonNull PendingIntent event_positive,
                @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        long currentTimeMillis = System.currentTimeMillis();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.set(Calendar.HOUR_OF_DAY, data.time.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, data.time.get(Calendar.MINUTE));
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTimeMillis);
        if (calendar.before(now)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            newSatisfiedState(true);
        } else {
            newSatisfiedState(false);
        }
    }

    private static Calendar[] calendarOfToday(Calendar ref) {
        // target time of today
        Calendar calendar_target = Calendar.getInstance(), calendar_zero = Calendar.getInstance(), now = Calendar.getInstance();
        long currentTimeMillis = System.currentTimeMillis();
        calendar_target.setTimeInMillis(currentTimeMillis);
        calendar_target.set(Calendar.HOUR_OF_DAY, ref.get(Calendar.HOUR_OF_DAY));
        calendar_target.set(Calendar.MINUTE, ref.get(Calendar.MINUTE));

        // 0:00 of the next day (24:00 of today)
        calendar_zero.setTimeInMillis(currentTimeMillis);
        calendar_zero.set(Calendar.HOUR_OF_DAY, 0);
        calendar_zero.set(Calendar.MINUTE, 0);
        calendar_zero.set(Calendar.SECOND, 0);
        calendar_zero.roll(Calendar.DAY_OF_YEAR, 1);

        // current time
        now.setTimeInMillis(currentTimeMillis);

        return new Calendar[]{calendar_target, calendar_zero, now};
    }

    @Override
    public void start() {
        Calendar[] calendars = calendarOfToday(data.time);
        if (calendars[0].before(calendars[2]))
            calendars[0].add(Calendar.DAY_OF_YEAR, 1);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendars[0].getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, notifySelfIntent_positive);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendars[1].getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, notifySelfIntent_negative);
    }

    @Override
    public void stop() {
        mAlarmManager.cancel(notifySelfIntent_positive);
        mAlarmManager.cancel(notifySelfIntent_negative);
    }
}
