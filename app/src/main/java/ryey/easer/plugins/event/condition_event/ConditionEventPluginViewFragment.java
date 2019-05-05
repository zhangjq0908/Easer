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

package ryey.easer.plugins.event.condition_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.commons.ui.DataSelectSpinnerWrapper;
import ryey.easer.core.data.storage.ConditionDataStorage;
import ryey.easer.plugins.PluginViewFragment;

public class ConditionEventPluginViewFragment extends PluginViewFragment<ConditionEventEventData> {

    private DataSelectSpinnerWrapper sw_condition;
    private RadioGroup rg_condition_event;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__condition_event, container, false);
        sw_condition = new DataSelectSpinnerWrapper(getContext(), (Spinner) view.findViewById(R.id.spinner_condition));
        sw_condition
                .beginInit()
                .setAllowEmpty(false)
                .fillData(new ConditionDataStorage(getContext()).list())
                .finalizeInit();
        rg_condition_event = view.findViewById(R.id.rg_condition_event);
        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull ConditionEventEventData data) {
        sw_condition.setSelection(data.conditionName);
        if (data.conditionEvent == ConditionEventEventData.ConditionEvent.enter) {
            rg_condition_event.check(R.id.rb_enter);
        } else {
            rg_condition_event.check(R.id.rb_leave);
        }
    }

    @ValidData
    @NonNull
    @Override
    public ConditionEventEventData getData() throws InvalidDataInputException {
        String condition_name = sw_condition.getSelection();
        ConditionEventEventData.ConditionEvent conditionEvent =
                rg_condition_event.getCheckedRadioButtonId() == R.id.rb_enter ?
                        ConditionEventEventData.ConditionEvent.enter :
                        ConditionEventEventData.ConditionEvent.leave;
        return new ConditionEventEventData(condition_name, conditionEvent);
    }
}
