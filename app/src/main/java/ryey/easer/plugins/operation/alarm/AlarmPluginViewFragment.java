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

package ryey.easer.plugins.operation.alarm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import java.text.ParseException;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.StorageData;

public class AlarmPluginViewFragment extends PluginViewFragment {
    private EditText editText_time;
    private EditText editText_message;
    private RadioButton radioButton_absolute, radioButton_relative;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__set_alarm, container, false);

        editText_time = view.findViewById(R.id.editText_time);
        editText_message = view.findViewById(R.id.editText_message);
        radioButton_absolute = view.findViewById(R.id.radioButton_absolute);
        radioButton_relative = view.findViewById(R.id.radioButton_relative);

        return view;
    }

    @Override
    protected void _fill(@NonNull StorageData data) {
        AlarmOperationData.AlarmData alarm_data = ((AlarmOperationData) data).data;
        editText_time.setText(AlarmOperationData.TimeToText(alarm_data.time));
        editText_message.setText(alarm_data.message);
        if (alarm_data.absolute)
            radioButton_absolute.setChecked(true);
        else
            radioButton_relative.setChecked(true);
    }

    @NonNull
    @Override
    public StorageData getData() throws InvalidDataInputException {
        AlarmOperationData.AlarmData data = new AlarmOperationData.AlarmData();
        try {
            data.time = AlarmOperationData.TextToTime(editText_time.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new InvalidDataInputException(e.getMessage());
        }
        data.message = editText_message.getText().toString();
        data.absolute = radioButton_absolute.isChecked();
        return new AlarmOperationData(data);
    }

}
