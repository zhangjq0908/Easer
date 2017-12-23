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

package ryey.easer.plugins.event.timer;

import android.app.AlarmManager;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.SelfNotifiableSlot;

public class TimerSlot extends SelfNotifiableSlot {
    private static AlarmManager mAlarmManager;

    private static final int INTERVAL_MINUTE = 60 * 1000;

    private TimerEventData.Timer timer;
    private EventType type = null;

    public TimerSlot(Context context) {
        super(context);

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void set(@NonNull EventData data) {
        if (data instanceof TimerEventData) {
            timer = ((TimerEventData) data).timer;
            type = data.type();
            if (timer.repeat) {
                setRetriggerable(true);
            } else {
                setRetriggerable(false);
            }
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public boolean isValid() {
        if (timer == null)
            return false;
        return true;
    }

    @Override
    public void listen() {
        super.listen();
        if (timer != null) {
            Calendar now = Calendar.getInstance();
            switch (type) {
                case after:
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
                    break;
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
        if (type == EventType.after) {
            changeSatisfiedState(true);
        }
    }
}