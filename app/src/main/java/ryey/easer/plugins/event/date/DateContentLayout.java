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

package ryey.easer.plugins.event.date;

import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;

import ryey.easer.R;
import ryey.easer.commons.ContentLayout;
import ryey.easer.commons.StorageData;

public class DateContentLayout extends ContentLayout.LabeledContentLayout {
    DatePicker datePicker;

    public DateContentLayout(Context context) {
        super(context);
        setDesc(context.getString(R.string.event_date));
        datePicker = new DatePicker(context);
        addView(datePicker);
    }

    private static Calendar fromDatePicker(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return calendar;
    }

    private static void setDatePicker(DatePicker datePicker, Calendar calendar) {
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);
    }

    @Override
    public void fill(StorageData data) {
        if (data instanceof DateEventData) {
            setDatePicker(datePicker, (Calendar) data.get());
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public StorageData getData() {
        return new DateEventData(fromDatePicker(datePicker));
    }
}
