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
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.format.DateUtils;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.Calendar;

import ryey.easer.skills.condition.SkeletonTracker;

public class CalendarTracker extends SkeletonTracker<CalendarUSourceData> {

    private static final String ACTION_UPDATE= "ryey.easer.skills.condition.calendar.UPDATE";

    private final Intent intentUpdate = new Intent(ACTION_UPDATE);
    private final IntentFilter intentUpdate_filter = new IntentFilter(ACTION_UPDATE);
    private final PendingIntent notifyIntent_change = PendingIntent.getBroadcast(context, 0, intentUpdate, 0);

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d("Intent received. action: %s", intent.getAction());
            if (intent.getAction().equals(ACTION_UPDATE)) {
                updateTracker();
            }
        }
    };

    private static AlarmManager mAlarmManager;

    private ContentObserver calendarObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            updateTracker();
        }
    };

    private final CalConditionInnerData innerData;

    CalendarTracker(Context context, CalendarUSourceData data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);
        this.innerData = (CalConditionInnerData) data.data;
        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void start() {
        context.registerReceiver(mReceiver, intentUpdate_filter);
        Uri uri = CalendarContract.Instances.CONTENT_URI;
        context.getContentResolver().registerContentObserver(uri, true, calendarObserver);
        updateTracker();
    }

    @Override
    public void stop() {
        context.unregisterReceiver(mReceiver);
        context.getContentResolver().unregisterContentObserver(calendarObserver);
        mAlarmManager.cancel(notifyIntent_change);
    }

    private void updateTracker() {
        Long nextRun;
        if (CalendarHelper.activeEventsCount(context.getContentResolver(), data.calendar_id, innerData) > 0) {
            newSatisfiedState(true);
            nextRun = CalendarHelper.currentEvent_match_end(context.getContentResolver(), data.calendar_id, innerData);
        } else {
            newSatisfiedState(false);
            nextRun = CalendarHelper.nextEvent_match_start(context.getContentResolver(), data.calendar_id, innerData);
        }
        if (nextRun == null)
            nextRun = Calendar.getInstance().getTimeInMillis() + DateUtils.DAY_IN_MILLIS;
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, nextRun, notifyIntent_change);
    }
}
