/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.event.time;

import android.app.AlarmManager;
import android.content.Context;

import java.util.Calendar;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.SelfNotifiableSlot;

public class TimeSlot extends SelfNotifiableSlot {
    static AlarmManager mAlarmManager;

    Calendar calendar = null;
    EventType type = null;

    public TimeSlot(Context context) {
        super(context);

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void set(EventData data) {
        if (data instanceof TimeEventData) {
            setTime((Calendar) data.get());
            type = data.type();
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    public void setTime(Calendar time) {
        if (time == null)
            return;
        if (calendar == null) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        calendar.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
    }

    @Override
    public boolean isValid() {
        if (calendar == null)
            return false;
        return true;
    }

    @Override
    public void listen() {
        if (calendar != null) {
            mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, notifySelfIntent);
        }
    }

    @Override
    public void cancel() {
        if (calendar != null) {
            mAlarmManager.cancel(notifySelfIntent);
        }
    }

    @Override
    public void check() {
        Calendar cal = Calendar.getInstance();
        if (cal.after(calendar)) {
            changeSatisfiedState(true);
        } else {
            changeSatisfiedState(false);
        }
    }

    @Override
    protected void onNotified() {
        if (type == EventType.after) {
            changeSatisfiedState(true);
        }
    }
}