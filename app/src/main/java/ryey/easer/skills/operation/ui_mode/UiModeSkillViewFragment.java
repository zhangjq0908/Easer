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

package ryey.easer.skills.operation.ui_mode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class UiModeSkillViewFragment extends SkillViewFragment<UiModeOperationData> {

    RadioButton rb_car, rb_normal;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skill_operation__ui_mode, container, false);

        rb_car = view.findViewById(R.id.rb_car);
        rb_normal = view.findViewById(R.id.rb_normal);

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull UiModeOperationData data) {
        if (data.ui_mode == UiModeOperationData.UiMode.car) {
            rb_car.setChecked(true);
        } else { // if data.ui_mode == UiModeOperationData.UiMode.normal) {
            rb_normal.setChecked(true);
        }
    }

    @ValidData
    @NonNull
    @Override
    public UiModeOperationData getData() throws InvalidDataInputException {
        if (rb_car.isChecked()) {
            return new UiModeOperationData(UiModeOperationData.UiMode.car);
        } else { // if (rb_normal.isChecked()) {
            return new UiModeOperationData(UiModeOperationData.UiMode.normal);
        }
    }
}
