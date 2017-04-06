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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import ryey.easer.R;
import ryey.easer.commons.plugindef.ContentLayout;
import ryey.easer.commons.plugindef.StorageData;

public class CellLocationContentLayout extends ContentLayout {
    EditText editText;

    public CellLocationContentLayout(Context context) {
        super(context);
        setDesc(context.getString(R.string.event_celllocation));
        editText = new EditText(context);
        editText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageButton imgButton = new ImageButton(context);
        imgButton.setImageResource(R.drawable.ic_add_location_black);
        imgButton.setOnClickListener(new OnClickListener() {
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
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        layout.addView(imgButton);
        layout.addView(editText);
        addView(layout);
    }

    @Override
    public void fill(StorageData data) {
        if (data instanceof CellLocationEventData) {
            editText.setText(data.toString());
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public StorageData getData() {
        return CellLocationEventData.fromString(editText.getText().toString());
    }
}
