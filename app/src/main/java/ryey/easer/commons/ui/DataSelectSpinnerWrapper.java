/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.commons.ui;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;

public class DataSelectSpinnerWrapper {

    private Context context;
    private Spinner spinner;

    private static final String NON = ""; //TODO: more robust

    private boolean allowEmpty = false;

    private List<String> augmentedList = new ArrayList<>();

    public DataSelectSpinnerWrapper(Context context, Spinner spinner) {
        this.context = context;
        this.spinner = spinner;
    }

    public DataSelectSpinnerWrapper beginInit() {
        this.augmentedList.clear();
        return this;
    }

    public DataSelectSpinnerWrapper setAllowEmpty(boolean state) {
        this.allowEmpty = state;
        return this;
    }

    public DataSelectSpinnerWrapper fillData(List<String> dataList) {
        augmentedList.addAll(dataList);
        return this;
    }

    public void finalizeInit() {
        if (allowEmpty)
            augmentedList.add(0, NON);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_simple, augmentedList); //TODO: change layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void setSelection(String selection) {
        if (allowEmpty && selection == null)
            spinner.setSelection(0);
        else
            spinner.setSelection(augmentedList.indexOf(selection));
    }

    public String getSelection() {
        String selection = (String) spinner.getSelectedItem();
        if (allowEmpty && selection.equals(NON))
            return null;
        return selection;
    }
}
