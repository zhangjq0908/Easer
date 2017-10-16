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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;

import ryey.easer.R;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.plugins.event.TypedContentFragment;

public class DateContentFragment extends TypedContentFragment {
    DatePicker datePicker;

    {
        expectedDataClass = DateEventData.class;
        setDesc(R.string.event_date);
    }

    @NonNull
    @Override
    public ViewGroup onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view_container = super.onCreateView(inflater, container, savedInstanceState);
        setAvailableTypes(new DateEventData().availableTypes());
        setType(new DateEventData().type());

        datePicker = new DatePicker(getContext());
        view_container.addView(datePicker);

        return view_container;
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
    protected void _fill(StorageData data) {
        if (data instanceof DateEventData) {
            setDatePicker(datePicker, (Calendar) data.get());
        }
    }

    @Override
    public StorageData getData() {
        return new DateEventData(fromDatePicker(datePicker), selectedType());
    }
}
