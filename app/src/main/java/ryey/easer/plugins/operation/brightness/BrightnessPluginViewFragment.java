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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.StorageData;

public class BrightnessPluginViewFragment extends PluginViewFragment {
    Switch mIsAuto;
    SeekBar mBrightnessLevel;

    {
        expectedDataClass = BrightnessOperationData.class;
        setDesc(R.string.operation_brightness);
    }

    @NonNull
    @Override
    public ViewGroup onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout view = new LinearLayout(getContext());
        view.setOrientation(LinearLayout.VERTICAL);

        LinearLayout auto_layout = new LinearLayout(getContext());
        auto_layout.setOrientation(LinearLayout.HORIZONTAL);
        TextView tv_auto = new TextView(getContext());
        tv_auto.setText(getResources().getString(R.string.operation_brightness_desc_autobrightness));
        mIsAuto = new Switch(getContext());
        mIsAuto.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBrightnessLevel = new SeekBar(getContext());
        mBrightnessLevel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

        view.addView(auto_layout);
        view.addView(mBrightnessLevel);

        return view;
    }

    @Override
    protected void _fill(StorageData data) {
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
