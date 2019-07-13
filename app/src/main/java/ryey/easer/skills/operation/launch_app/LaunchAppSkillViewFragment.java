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

package ryey.easer.skills.operation.launch_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.skills.operation.EditExtraFragment;

public class LaunchAppSkillViewFragment extends SkillViewFragment<LaunchAppOperationData> {
    EditText et_app_package;
    EditText et_class;
    EditExtraFragment editExtraFragment;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__launch_app, container, false);
        et_app_package = view.findViewById(R.id.editText_app_package);
        et_class = view.findViewById(R.id.editText_app_activity);
        editExtraFragment = (EditExtraFragment) getChildFragmentManager().findFragmentById(R.id.fragment_edit_extra);
        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull LaunchAppOperationData data) {
        et_app_package.setText(data.app_package);
        et_class.setText(data.app_class);
        editExtraFragment.fillExtras(data.extras);
    }

    @ValidData
    @NonNull
    @Override
    public LaunchAppOperationData getData() throws InvalidDataInputException {
        return new LaunchAppOperationData(et_app_package.getText().toString(), et_class.getText().toString(), editExtraFragment.getExtras());
    }
}
