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

package ryey.easer.skills.combined.battery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

import static android.widget.LinearLayout.HORIZONTAL;

public class BatterySkillViewFragment extends SkillViewFragment<BatteryCombinedSourceData> {
    String []mode_names;
    final int []values = {
            BatteryStatus.charging,
            BatteryStatus.discharging
    };
    final RadioButton []radioButtons = new RadioButton[values.length];

    private Integer checked_item = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode_names = getResources().getStringArray(R.array.battery_status);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(HORIZONTAL);
        View.OnClickListener radioButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < radioButtons.length; i++) {
                    if (v == radioButtons[i]) {
                        checked_item = i;
                        break;
                    }
                }
            }
        };
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i] = new RadioButton(getContext());
            radioButtons[i].setText(mode_names[i]);
            radioButtons[i].setOnClickListener(radioButtonOnClickListener);
            radioGroup.addView(radioButtons[i]);
        }
        return radioGroup;
    }

    @Override
    protected void _fill(@ValidData @NonNull BatteryCombinedSourceData data) {
        int status = data.battery_status;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == status) {
                radioButtons[i].setChecked(true);
                checked_item = i;
            }
        }
    }

    @ValidData
    @NonNull
    @Override
    public BatteryCombinedSourceData getData() throws InvalidDataInputException {
        if (checked_item == null)
            throw new InvalidDataInputException();
        return new BatteryCombinedSourceData(values[checked_item]);
    }
}
