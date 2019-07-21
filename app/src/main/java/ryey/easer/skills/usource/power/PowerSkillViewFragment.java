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

package ryey.easer.skills.usource.power;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class PowerSkillViewFragment extends SkillViewFragment<PowerUSourceData> {

    final ChargingMethod []values = {
            ChargingMethod.any,
            ChargingMethod.ac,
            ChargingMethod.usb,
    };
    final int []checkBoxIds = {
            R.id.cb_any,
            R.id.cb_ac,
            R.id.cb_usb,
    };
    final CheckBox []checkBoxes = new CheckBox[checkBoxIds.length];

    RadioGroup rg_status;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skill_usource__power_status, container, false);

        rg_status = view.findViewById(R.id.rg_status);
        ViewGroup layout_charging_tune = view.findViewById(R.id.layout_charging_tune);
        rg_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_charging)
                    layout_charging_tune.setVisibility(View.VISIBLE);
                else if (checkedId == R.id.rb_discharging)
                    layout_charging_tune.setVisibility(View.GONE);
                else
                    throw new IllegalStateException("Charging status radio group check changed to unknown element");
            }
        });

        for (int i = 0; i < checkBoxIds.length; i++) {
            checkBoxes[i] = view.findViewById(checkBoxIds[i]);
        }

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull PowerUSourceData data) {
        if (data.batteryStatus == BatteryStatus.discharging) {
            rg_status.check(R.id.rb_discharging);
        } else {
            rg_status.check(R.id.rb_charging);
            for (int i = 0; i < checkBoxIds.length; i++) {
                if (data.chargingMethods.contains(values[i])) {
                    checkBoxes[i].setChecked(true);
                } else {
                    checkBoxes[i].setChecked(false);
                }
            }
        }
    }

    @ValidData
    @NonNull
    @Override
    public PowerUSourceData getData() throws InvalidDataInputException {
        if (rg_status.getCheckedRadioButtonId() == R.id.rb_discharging)
            return new PowerUSourceData(BatteryStatus.discharging, null);
        List<ChargingMethod> chargingMethods = new ArrayList<>();
        for (int i = 0; i < checkBoxIds.length; i++) {
            if (checkBoxes[i].isChecked())
                chargingMethods.add(values[i]);
        }
        if (chargingMethods.size() == 0)
            throw new InvalidDataInputException();
        return new PowerUSourceData(BatteryStatus.charging, chargingMethods);
    }
}
