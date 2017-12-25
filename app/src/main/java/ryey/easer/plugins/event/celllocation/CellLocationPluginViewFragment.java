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

package ryey.easer.plugins.event.celllocation;

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

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.StorageData;

public class CellLocationPluginViewFragment extends PluginViewFragment<CellLocationEventData> {
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
                @SuppressLint("MissingPermission") CellLocationSingleData singleData = CellLocationSingleData.fromCellLocation(telephonyManager.getCellLocation());
                CellLocationEventData locationData = CellLocationEventData.fromString(editText.getText().toString());
                if (locationData == null)
                    locationData = new CellLocationEventData();
                locationData.add(singleData);
                editText.setText(locationData.toString());
            }
        });

        return view;
    }

    @Override
    protected void _fill(@NonNull CellLocationEventData data) {
        if (data instanceof CellLocationEventData) {
            editText.setText(data.toString());
        }
    }

    @NonNull
    @Override
    public CellLocationEventData getData() throws InvalidDataInputException {
        CellLocationEventData data = CellLocationEventData.fromString(editText.getText().toString());
        return data;
    }
}
