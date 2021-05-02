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

package ryey.easer.skills.operation.wireguard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.commons.local_skill.ValidData;

public class WireguardSkillViewFragment extends SkillViewFragment<WireguardOperationData> {
    private EditText et_tunnel_name;
    private Spinner spinner_tunnel_state;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skill_operation__wireguard, container, false);
        et_tunnel_name = view.findViewById(R.id.editText_tunnel_name);
        spinner_tunnel_state = view.findViewById(R.id.spinner_tunnel_state);
        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull WireguardOperationData data) {
        et_tunnel_name.setText(data.tunnel_name);
        spinner_tunnel_state.setSelection(data.tunnel_state ? 0 : 1);
    }

    @ValidData
    @NonNull
    @Override
    public WireguardOperationData getData() throws InvalidDataInputException {
        return new WireguardOperationData(et_tunnel_name.getText().toString(), spinner_tunnel_state.getSelectedItemPosition() == 0);
    }
}
