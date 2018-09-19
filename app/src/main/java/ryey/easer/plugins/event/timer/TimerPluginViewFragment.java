/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.plugins.PluginViewFragment;
import ryey.easer.commons.local_plugin.ValidData;

public class TimerPluginViewFragment extends PluginViewFragment<TimerEventData> {
    private EditText editText_minute;

    private RadioButton radioButton_exact, radioButton_inexact;
    private RadioButton radioButton_repeat, radioButton_one_time;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__timer, container, false);
        editText_minute = view.findViewById(R.id.editText_minute);
        radioButton_exact = view.findViewById(R.id.radioButton_exact);
        radioButton_inexact = view.findViewById(R.id.radioButton_inexact);
        radioButton_repeat = view.findViewById(R.id.radioButton_repeat);
        radioButton_one_time = view.findViewById(R.id.radioButton_one_time);
        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull TimerEventData data) {
        editText_minute.setText(String.valueOf(data.minutes));
        if (data.exact)
            radioButton_exact.setChecked(true);
        else
            radioButton_inexact.setChecked(true);
        if (data.repeat)
            radioButton_repeat.setChecked(true);
        else
            radioButton_one_time.setChecked(true);
    }

    @ValidData
    @NonNull
    @Override
    public TimerEventData getData() throws InvalidDataInputException {
        try {
            int minutes = Integer.parseInt(editText_minute.getText().toString());
            boolean exact = radioButton_exact.isChecked();
            boolean repeat = radioButton_repeat.isChecked();
            return new TimerEventData(minutes, exact, repeat);
        } catch (NumberFormatException e) {
            throw new InvalidDataInputException(e.getMessage());
        }
    }
}
