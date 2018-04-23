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

package ryey.easer.plugins.event.timer;

import android.app.AlarmManager;
import android.content.Context;

import java.util.Calendar;

import ryey.easer.plugins.event.SelfNotifiableSlot;

public class TimerSlot extends SelfNotifiableSlot<TimerEventData> {
    private static AlarmManager mAlarmManager;

    private static final int INTERVAL_MINUTE = 60 * 1000;

    private TimerEventData.Timer timer;

    public TimerSlot(Context context, TimerEventData data) {
        this(context, data, isRetriggerable(data), PERSISTENT_DEFAULT);
    }

    TimerSlot(Context context, TimerEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        timer = data.timer;

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private static boolean isRetriggerable(TimerEventData data) {
        TimerEventData.Timer timer = data.timer;
        if (timer.repeat) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void listen() {
        super.listen();
        if (timer != null) {
            Calendar now = Calendar.getInstance();
            if (timer.exact) {
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        now.getTimeInMillis() + INTERVAL_MINUTE * timer.minutes,
                        INTERVAL_MINUTE * timer.minutes,
                        notifySelfIntent_positive);
            } else {
                mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        now.getTimeInMillis() + INTERVAL_MINUTE * timer.minutes,
                        INTERVAL_MINUTE * timer.minutes,
                        notifySelfIntent_positive);
            }
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (timer != null) {
            mAlarmManager.cancel(notifySelfIntent_positive);
            mAlarmManager.cancel(notifySelfIntent_negative);
        }
    }

    @Override
    public void check() {
    }

    @Override
    protected void onPositiveNotified() {
        changeSatisfiedState(true);
    }
}