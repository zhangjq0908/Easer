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

package ryey.easer.plugins.operation.event_control;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.core.data.storage.EventDataStorage;

public class EventControlPluginViewFragment extends PluginViewFragment<EventControlOperationData> {

    List<String> mEventList = null;
    private Spinner spinner_event;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__event_control, container, false);
        spinner_event = view.findViewById(R.id.spinner_event);
        mEventList = (EventDataStorage.getInstance(getContext())).list();
        //noinspection ConstantConditions
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_simple, mEventList); //TODO: change layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_event.setAdapter(adapter);
        spinner_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    protected void _fill(@ValidData @NonNull EventControlOperationData data) {
        spinner_event.setSelection(mEventList.indexOf(data.eventName));
    }

    @ValidData
    @NonNull
    @Override
    public EventControlOperationData getData() throws InvalidDataInputException {
        String eventName = (String) spinner_event.getSelectedItem();
        return new EventControlOperationData(eventName, false);
    }
}
