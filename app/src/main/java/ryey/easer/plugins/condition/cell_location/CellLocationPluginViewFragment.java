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

package ryey.easer.plugins.condition.cell_location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.PluginViewFragment;

public class CellLocationPluginViewFragment extends PluginViewFragment<CellLocationConditionData> {
    private EditText editText;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__cell_location, container, false);

        editText = view.findViewById(R.id.location_text);
        view.findViewById(R.id.location_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.hasPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION))
                    return;
                TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager == null) {
                    Toast.makeText(getContext(), R.string.condition_cell_location_no_signal, Toast.LENGTH_SHORT).show();
                    return;
                }
                @SuppressLint("MissingPermission") CellLocationSingleData singleData = CellLocationSingleData.fromCellLocation(telephonyManager.getCellLocation());
                if (singleData == null) {
                    Toast.makeText(getContext(), R.string.condition_cell_location_no_signal, Toast.LENGTH_SHORT).show();
                    return;
                }
                String display_str = editText.getText().toString();
                if (Utils.isBlank(display_str)) {
                    editText.setText(singleData.toString());
                } else {
                    display_str += "\n" + singleData.toString();
                    editText.setText(display_str);
                }
            }
        });

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull CellLocationConditionData data) {
        editText.setText(data.toString());
    }

    @ValidData
    @NonNull
    @Override
    public CellLocationConditionData getData() throws InvalidDataInputException {
        CellLocationConditionData data = new CellLocationConditionData(editText.getText().toString().split("\n"));
        if (data.isValid())
            return data;
        throw new InvalidDataInputException();
    }
}
