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

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;

import ryey.easer.R;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.plugins.event.TypedContentLayout;

public class CellLocationContentLayout extends TypedContentLayout {
    EditText editText;

    {
        expectedDataClass = CellLocationEventData.class;
    }

    public CellLocationContentLayout(Context context) {
        super(context);
        setAvailableTypes(new CellLocationEventData().availableTypes());
        setType(new CellLocationEventData().type());
        setDesc(context.getString(R.string.event_celllocation));
        inflate(context, R.layout.plugin_event__cell_location, this);
        editText = (EditText) findViewById(R.id.location_text);
        findViewById(R.id.location_picker).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                CellLocationSingleData singleData = CellLocationSingleData.fromCellLocation(telephonyManager.getCellLocation());
                CellLocationEventData locationData = CellLocationEventData.fromString(editText.getText().toString());
                if (locationData == null)
                    locationData = new CellLocationEventData();
                locationData.add(singleData);
                editText.setText(locationData.toString());
            }
        });
    }

    @Override
    protected void _fill(StorageData data) {
        if (data instanceof CellLocationEventData) {
            super.fill(data);
            editText.setText(data.toString());
        }
    }

    @Override
    public StorageData getData() {
        CellLocationEventData data = CellLocationEventData.fromString(editText.getText().toString());
        if (data != null) {
            data.setType(selectedType());
        }
        return data;
    }
}
