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

package ryey.easer.plugins.operation.brightness;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import ryey.easer.R;
import ryey.easer.commons.plugindef.ContentLayout;
import ryey.easer.commons.plugindef.StorageData;

public class BrightnessContentLayout extends ContentLayout {
    Switch mIsAuto;
    SeekBar mBrightnessLevel;

    public BrightnessContentLayout(Context context) {
        super(context);
        setDesc(context.getString(R.string.operation_brightness));

        LinearLayout auto_layout = new LinearLayout(context);
        auto_layout.setOrientation(HORIZONTAL);
        TextView tv_auto = new TextView(context);
        tv_auto.setText(getResources().getString(R.string.operation_brightness_desc_autobrightness));
        mIsAuto = new Switch(context);
        mIsAuto.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mBrightnessLevel = new SeekBar(context);
        mBrightnessLevel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mBrightnessLevel.setMax(255);
        mBrightnessLevel.setEnabled(false);
        mIsAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mBrightnessLevel.setEnabled(false);
                else
                    mBrightnessLevel.setEnabled(true);
            }
        });
        auto_layout.addView(mIsAuto);
        auto_layout.addView(tv_auto);
        addView(auto_layout);
        addView(mBrightnessLevel);
    }

    @Override
    public void fill(StorageData data) {
        BrightnessOperationData idata = (BrightnessOperationData) data;
        if (idata.isAuto()) {
            mIsAuto.setChecked(true);
        } else {
            mIsAuto.setChecked(false);
            mBrightnessLevel.setProgress((Integer) idata.get());
        }
    }

    @Override
    public StorageData getData() {
        if (mIsAuto.isChecked())
            return new BrightnessOperationData(true);
        else {
            return new BrightnessOperationData(mBrightnessLevel.getProgress());
        }
    }
}
