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

package ryey.easer.plugins.event.date;

import android.app.AlarmManager;
import android.content.Context;

import java.util.Calendar;

import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.SelfNotifiableSlot;

/*
 * TODO: cancel (or set extra) alarm after being satisfied or unsatisfied (for different event types)
 */
public class DateSlot extends SelfNotifiableSlot<DateEventData> {
    private static AlarmManager mAlarmManager;

    private Calendar calendar = null;
    private EventType type = null;

    public DateSlot(Context context, DateEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    DateSlot(Context context, DateEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        setDate(data.date);
        type = data.type();

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private void setDate(Calendar date) {
        if (date == null)
            return;
        if (calendar == null) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(0);
        }
        calendar.setTime(date.getTime());
    }

    @Override
    public void listen() {
        super.listen();
        if (calendar != null) {
            switch (type) {
                case after:
                case is:
                    mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, notifySelfIntent_positive);
                    break;
                case before:
                    mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, notifySelfIntent_negative);
                    break;
            }
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
    public void check() {
        Calendar cal = Calendar.getInstance();
        if (type == EventType.after) {
            if (cal.get(Calendar.DAY_OF_YEAR) >= calendar.get(Calendar.DAY_OF_YEAR)) {
                changeSatisfiedState(true);
            } else {
                changeSatisfiedState(false);
            }
        }
        if (type == EventType.before) {
            if (cal.get(Calendar.DAY_OF_YEAR) <= calendar.get(Calendar.DAY_OF_YEAR)) {
                changeSatisfiedState(true);
            } else {
                changeSatisfiedState(false);
            }
        }
        if (type == EventType.is) {
            boolean match = true;
            for (int field : new int[]{Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH}) {
                if ((cal.get(field) != calendar.get(field))) {
                    match = false;
                    break;
                }
            }
            if (match)
                changeSatisfiedState(true);
            else
                changeSatisfiedState(false);
        }
    }

    @Override
    protected void onPositiveNotified() {
        if (type == EventType.after) {
            changeSatisfiedState(true);
        }
        if (type == EventType.is) {
            changeSatisfiedState(true);
        }
    }
}
