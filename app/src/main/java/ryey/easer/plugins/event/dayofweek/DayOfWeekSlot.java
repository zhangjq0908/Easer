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

package ryey.easer.plugins.event.dayofweek;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.util.Calendar;
import java.util.Set;

import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.SelfNotifiableSlot;

class DayOfWeekSlot extends SelfNotifiableSlot<DayOfWeekEventData> {
    private static AlarmManager mAlarmManager;

    private Set<Integer> days;
    private EventType type = null;

    public DayOfWeekSlot(Context context, DayOfWeekEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTEN_DEFAULT);
    }

    DayOfWeekSlot(Context context, DayOfWeekEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        setDate(data.days);
        type = data.type();

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private void setDate(Set<Integer> days) {
        if (days == null)
            return;
        this.days = days;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void listen() {
        super.listen();
        switch (type) {
            case any:
                scheduleAlarms(notifySelfIntent_positive);
                break;
            case none:
                scheduleAlarms(notifySelfIntent_negative);
                break;
        }
    }

    private void scheduleAlarms(PendingIntent pendingIntent) {
        for (int dayOfWeek : days) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek + 1);
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                if (!(calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)))
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
            }
            mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        mAlarmManager.cancel(notifySelfIntent_positive);
        mAlarmManager.cancel(notifySelfIntent_negative);
    }

    @Override
    public void check() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (type) {
            case any:
                if (days.contains(dayOfWeek - 1))
                    changeSatisfiedState(true);
                else
                    changeSatisfiedState(false);
                break;
            case none:
                if (days.contains(dayOfWeek - 1))
                    changeSatisfiedState(false);
                else
                    changeSatisfiedState(true);
                break;
        }
    }
}
