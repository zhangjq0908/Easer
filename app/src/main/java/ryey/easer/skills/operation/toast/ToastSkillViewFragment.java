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

package ryey.easer.skills.operation.toast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.skills.operation.DynamicsEnabledString;

public class ToastSkillViewFragment extends SkillViewFragment<ToastOperationData> {

    private TextInputLayout textInputLayout;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skill_operation__toast, container, false);

        textInputLayout = view.findViewById(R.id.text_input);

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull ToastOperationData data) {
        Objects.requireNonNull(textInputLayout.getEditText()).setText(data.text.str);
    }

    @ValidData
    @NonNull
    @Override
    public ToastOperationData getData() throws InvalidDataInputException {
        return new ToastOperationData(DynamicsEnabledString.fromView(Objects.requireNonNull(textInputLayout.getEditText())));
    }
}
