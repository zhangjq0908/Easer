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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.core.data.storage.ConditionDataStorage;

public class ConditionEventPluginViewFragment extends PluginViewFragment<ConditionEventEventData> {

    private Spinner spinner_condition;
    private RadioGroup rg_condition_event;
    private List<String> mConditionList;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__condition_event, container, false);
        spinner_condition = view.findViewById(R.id.spinner_condition);
        rg_condition_event = view.findViewById(R.id.rg_condition_event);
        mConditionList = ConditionDataStorage.getInstance(getContext()).list();
        ArrayAdapter<String> adapter_condition = new ArrayAdapter<>(getContext(), R.layout.spinner_simple, mConditionList);
        adapter_condition.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_condition.setAdapter(adapter_condition);
        spinner_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull ConditionEventEventData data) {
        spinner_condition.setSelection(mConditionList.indexOf(data.conditionName));
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
        String condition_name = (String) spinner_condition.getSelectedItem();
        ConditionEventEventData.ConditionEvent conditionEvent =
                rg_condition_event.getCheckedRadioButtonId() == R.id.rb_enter ?
                        ConditionEventEventData.ConditionEvent.enter :
                        ConditionEventEventData.ConditionEvent.leave;
        return new ConditionEventEventData(condition_name, conditionEvent);
    }
}
