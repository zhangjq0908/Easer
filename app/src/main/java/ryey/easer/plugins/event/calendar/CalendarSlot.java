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

package ryey.easer.plugins.event.calendar;

import android.app.AlarmManager;
import android.content.Context;

import ryey.easer.commons.IllegalArgumentTypeException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.SelfNotifiableSlot;

public class CalendarSlot extends SelfNotifiableSlot {

    static AlarmManager mAlarmManager = null;

    private CalendarEventData data = null;
    private EventType type = null;

    public CalendarSlot(Context context) {
        super(context);
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void set(EventData data) {
        if (data instanceof CalendarEventData) {
            this.data = (CalendarEventData) data;
            type = data.type();
        } else {
            throw new IllegalArgumentTypeException(data.getClass(), CalendarEventData.class);
        }
    }

    @Override
    public boolean isValid() {
        return data.isValid();
    }

    @Override
    public void listen() {
        if (data.data.conditions.get(CalendarData.condition_name[0])) {
            Long time_next_event = CalendarHelper.nextEvent_start(context.getContentResolver(), data.data.calendar_id);
            if (time_next_event != null) {
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, time_next_event,
                        notifySelfIntent_positive);
            }
        }
        if (data.data.conditions.get(CalendarData.condition_name[1])) {
            Long time_next_event = CalendarHelper.nextEvent_end(context.getContentResolver(), data.data.calendar_id);
            if (time_next_event != null) {
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, time_next_event,
                        notifySelfIntent_positive);
            }
        }
    }

    @Override
    public void cancel() {
        mAlarmManager.cancel(notifySelfIntent_positive);
    }

    @Override
    public void check() {
        // Does nothing for semantics
    }

    @Override
    public boolean canPromoteSub() {
        return false;
    }

    @Override
    protected void onPositiveNotified() {
        changeSatisfiedState(true);
        changeSatisfiedState(false);
    }
}
