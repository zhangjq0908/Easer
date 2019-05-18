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

package ryey.easer.skills.event.calendar;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

import ryey.easer.skills.event.SelfNotifiableSlot;

public class CalendarSlot extends SelfNotifiableSlot<CalendarEventData> {

    private static AlarmManager mAlarmManager = null;

    public CalendarSlot(Context context, CalendarEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    CalendarSlot(Context context, CalendarEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void listen() {
        super.listen();
        if (eventData.data.conditions.contains(CalendarData.condition_name[0])) {
            Long time_next_event = CalendarHelper.nextEvent_start(context.getContentResolver(), eventData.data.calendar_id);
            if (time_next_event != null) {
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, time_next_event,
                        notifySelfIntent_positive);
            }
        }
        if (eventData.data.conditions.contains(CalendarData.condition_name[1])) {
            Long time_next_event = CalendarHelper.nextEvent_end(context.getContentResolver(), eventData.data.calendar_id);
            if (time_next_event != null) {
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, time_next_event,
                        notifySelfIntent_positive);
            }
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        mAlarmManager.cancel(notifySelfIntent_positive);
    }

    @Override
    protected void onPositiveNotified(Intent intent) {
        changeSatisfiedState(true);
        changeSatisfiedState(false);
    }
}
