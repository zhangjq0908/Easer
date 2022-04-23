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
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.skills.condition.SelfNotifiableSkeletonTracker;

public class DayOfWeekTracker extends SelfNotifiableSkeletonTracker<DayOfWeekUSourceData> {
    private static AlarmManager mAlarmManager;

    DayOfWeekTracker(Context context, DayOfWeekUSourceData data,
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

    @Override
    public void start() {
        super.start();
        Utils.scheduleAlarmEveryday(mAlarmManager, notifySelfIntent_positive);
    }

    @Override
    public void stop() {
        super.stop();
        mAlarmManager.cancel(notifySelfIntent_positive);
    }

    @Override
    protected void onPositiveNotified() {
        if (Utils.isSatisfied(data.days)) {
            newSatisfiedState(true);
        } else {
            newSatisfiedState(false);
        }
    }

}
