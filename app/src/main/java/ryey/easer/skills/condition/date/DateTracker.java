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

package ryey.easer.skills.condition.date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.skills.condition.SelfNotifiableSkeletonTracker;

public class DateTracker extends SelfNotifiableSkeletonTracker<DateConditionData> {
    private static AlarmManager mAlarmManager;

    DateTracker(Context context, DateConditionData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_YEAR) >= data.date.get(Calendar.DAY_OF_YEAR)) {
            newSatisfiedState(true);
        } else {
            newSatisfiedState(false);
        }
    }

    @Override
    public void start() {
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, data.date.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, notifySelfIntent_positive);
    }

    @Override
    public void stop() {
        mAlarmManager.cancel(notifySelfIntent_positive);
        mAlarmManager.cancel(notifySelfIntent_negative);
    }
}
