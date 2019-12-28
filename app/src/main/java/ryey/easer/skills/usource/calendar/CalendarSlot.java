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

package ryey.easer.skills.usource.calendar;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

import ryey.easer.skills.event.SelfNotifiableSlot;

public class CalendarSlot extends SelfNotifiableSlot<CalendarUSourceData> {

    private static AlarmManager mAlarmManager = null;

    private CalEventInnerData eventData;

    CalendarSlot(Context context, CalendarUSourceData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    CalendarSlot(Context context, CalendarUSourceData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        this.eventData = (CalEventInnerData) data.data;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void listen() {
        super.listen();
        if (eventData.conditions.contains(CalEventInnerData.condition_name[0])) {
            Long time_next_event = CalendarHelper.nextEvent_start(context.getContentResolver(), super.eventData.calendar_id);
            if (time_next_event != null) {
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, time_next_event,
                        notifySelfIntent_positive);
            }
        }
        if (eventData.conditions.contains(CalEventInnerData.condition_name[1])) {
            Long time_next_event = CalendarHelper.nextEvent_end(context.getContentResolver(), super.eventData.calendar_id);
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
