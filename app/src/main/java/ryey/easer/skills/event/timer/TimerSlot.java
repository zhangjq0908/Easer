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

package ryey.easer.skills.event.timer;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.Calendar;

import ryey.easer.skills.event.SelfNotifiableSlot;

public class TimerSlot extends SelfNotifiableSlot<TimerEventData> {
    private static AlarmManager mAlarmManager;

    private static final int INTERVAL_SECOND = 1000;
    private static final int INTERVAL_MINUTE = 60 * 1000;

//    private CountDownTimer countDownTimer;
    private final Handler handler = new Handler();
    private Runnable job;

    TimerSlot(Context context, TimerEventData data) {
        this(context, data, isRetriggerable(data), PERSISTENT_DEFAULT);
    }

    TimerSlot(Context context, TimerEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private static boolean isRetriggerable(TimerEventData data) {
        if (data.repeat) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void listen() {
        super.listen();
        if (eventData != null) {
            if (eventData.shortTime) {
                job = new Runnable() {
                    @Override
                    public void run() {
                        changeSatisfiedState(true);
                        if (eventData.repeat)
                            handler.postDelayed(job, eventData.time * INTERVAL_SECOND);
                    }
                };
                handler.postDelayed(job, eventData.time * INTERVAL_SECOND);
//                if (eventData.repeat) {
//                    countDownTimer = new RepeatedTimer();
//                } else {
//                    countDownTimer = new CountDownTimer(eventData.time * INTERVAL_SECOND, eventData.time * INTERVAL_SECOND) {
//                        @Override
//                        public void onTick(long millisUntilFinished) {
//                            Logger.d("onTick");
//                        }
//
//                        @Override
//                        public void onFinish() {
//                            Logger.d("onFinish");
//                            changeSatisfiedState(true);
//                        }
//                    };
//                }
//                countDownTimer.start();
            } else {
                Calendar now = Calendar.getInstance();
                if (eventData.exact) {
                    mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            now.getTimeInMillis() + INTERVAL_MINUTE * eventData.time,
                            INTERVAL_MINUTE * eventData.time,
                            notifySelfIntent_positive);
                } else {
                    mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                            now.getTimeInMillis() + INTERVAL_MINUTE * eventData.time,
                            INTERVAL_MINUTE * eventData.time,
                            notifySelfIntent_positive);
                }
            }
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (eventData != null) {
            if (eventData.shortTime) {
//                if (countDownTimer != null)
//                    countDownTimer.cancel();
                handler.removeCallbacks(job);
            } else {
                mAlarmManager.cancel(notifySelfIntent_positive);
                mAlarmManager.cancel(notifySelfIntent_negative);
            }
        }
    }

    @Override
    protected void onPositiveNotified(Intent intent) {
        changeSatisfiedState(true);
    }

//    private class RepeatedTimer extends CountDownTimer {
//
//        RepeatedTimer() {
//            // Restart the timer after 100 times, to mimic infinite timer
//            super(eventData.time * INTERVAL_SECOND * 100, eventData.time * INTERVAL_SECOND);
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            Logger.d("onTick: <%s>", millisUntilFinished);
//            changeSatisfiedState(true);
//        }
//
//        @Override
//        public void onFinish() {
//            countDownTimer = new RepeatedTimer();
//            countDownTimer.start();
//        }
//    }
}