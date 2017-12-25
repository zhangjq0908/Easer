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

import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.StorageData;

public class DatePluginViewFragment extends PluginViewFragment {
    private DatePicker datePicker;

    @NonNull
    @Override
    public ViewGroup onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        datePicker = new DatePicker(getContext());
        return datePicker;
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
    protected void _fill(@NonNull StorageData data) {
        if (data instanceof DateEventData) {
            setDatePicker(datePicker, ((DateEventData) data).date);
        }
    }

    @NonNull
    @Override
    public StorageData getData() throws InvalidDataInputException {
        return new DateEventData(fromDatePicker(datePicker));
    }
}
