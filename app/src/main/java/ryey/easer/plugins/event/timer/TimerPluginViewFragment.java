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
import android.support.constraint.Group;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.PluginViewFragment;

public class TimerPluginViewFragment extends PluginViewFragment<TimerEventData> {
    private RadioButton radioButton_short, radioButton_long;

    Group group_short, group_long;

    private EditText editText_minute, editText_second;

    private RadioButton radioButton_exact, radioButton_inexact;
    private RadioButton radioButton_repeat, radioButton_one_time;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__timer, container, false);

        group_short = view.findViewById(R.id.group_short);
        group_long = view.findViewById(R.id.group_long);

        radioButton_short = view.findViewById(R.id.radioButton_short);
        radioButton_long = view.findViewById(R.id.radioButton_long);
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == radioButton_short) {
                    radioButton_long.setChecked(!isChecked);
                    group_short.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                } else if (buttonView == radioButton_long) {
                    radioButton_short.setChecked(!isChecked);
                    group_long.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                } else {
                    throw new IllegalStateException("This OnCheckedChangeListener shouldn't be used elsewhere.");
                }
            }
        };
        radioButton_short.setOnCheckedChangeListener(onCheckedChangeListener);
        radioButton_long.setOnCheckedChangeListener(onCheckedChangeListener);

        editText_second = view.findViewById(R.id.editText_second);

        editText_minute = view.findViewById(R.id.editText_minute);
        radioButton_exact = view.findViewById(R.id.radioButton_exact);
        radioButton_inexact = view.findViewById(R.id.radioButton_inexact);
        radioButton_repeat = view.findViewById(R.id.radioButton_repeat);
        radioButton_one_time = view.findViewById(R.id.radioButton_one_time);
        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull TimerEventData data) {
        if (data.shortTime) {
            radioButton_short.setChecked(true);
            editText_second.setText(String.valueOf(data.time));
        } else {
            editText_minute.setText(String.valueOf(data.time));
            if (data.exact)
                radioButton_exact.setChecked(true);
            else
                radioButton_inexact.setChecked(true);
        }
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
            boolean shortTime = radioButton_short.isChecked();
            boolean repeat = radioButton_repeat.isChecked();
            if (shortTime) {
                int seconds = Integer.parseInt(editText_second.getText().toString());
                return new TimerEventData(seconds, repeat);
            } else {
                int minutes = Integer.parseInt(editText_minute.getText().toString());
                boolean exact = radioButton_exact.isChecked();
                return new TimerEventData(minutes, exact, repeat);
            }
        } catch (NumberFormatException e) {
            throw new InvalidDataInputException(e.getMessage());
        }
    }
}
