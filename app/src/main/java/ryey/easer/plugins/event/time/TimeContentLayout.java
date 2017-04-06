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

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

import ryey.easer.R;
import ryey.easer.commons.ContentLayout;
import ryey.easer.commons.StorageData;

public class TimeContentLayout extends ContentLayout {
    TimePicker timePicker;

    public TimeContentLayout(Context context) {
        super(context);
        setDesc(context.getString(R.string.event_time));
        timePicker = new TimePicker(context);
        addView(timePicker);
    }

    private static void setTimePicker(TimePicker timePicker, Calendar calendar) {
        int hour, minute;
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        if (Build.VERSION.SDK_INT >= 23) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
    }

    private static Calendar fromTimePicker(TimePicker timePicker) {
        Calendar calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= 23) {
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        }
        return calendar;
    }

    @Override
    public void fill(StorageData data) {
        if (data instanceof TimeEventData) {
            setTimePicker(timePicker, (Calendar) data.get());
        } else if (data instanceof Calendar) {
            setTimePicker(timePicker, (Calendar) data);
        } else {
            Log.wtf(getClass().getSimpleName(), "filling with illegal data");
        }
    }

    @Override
    public StorageData getData() {
        return new TimeEventData(fromTimePicker(timePicker));
    }
}
