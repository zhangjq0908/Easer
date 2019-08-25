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

import java.util.Calendar;
import java.util.Collection;

public class Utils {

    static void scheduleAlarmEveryday(AlarmManager alarmManager, PendingIntent pendingIntent) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0); // 00:00
        calendar.roll(Calendar.DAY_OF_YEAR, 1); // "Tomorrow" (with extra care from the next code block)
        if (calendar.get(Calendar.DAY_OF_YEAR) == 0) // First day of year, but year is not rolled
            calendar.roll(Calendar.YEAR, 1);
        // calendar == Tomorrow 00:00
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    static boolean isSatisfied(Collection<Integer> days) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int day_of_week = now.get(Calendar.DAY_OF_WEEK) - 1; // Starts with 1, but ours starts with 0
        if (hour > 12) // If not yet 00:00 (because it's "inexact")
            day_of_week = (day_of_week + 1) % 7;
        return days.contains(day_of_week);
    }

}
