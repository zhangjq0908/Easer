/*
 * Copyright (c) 2016 - 2018, 2020 Rui Zhao <renyuneyun@gmail.com> and Daniel Landau
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

package ryey.easer.skills.operation.bluetooth_connect;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.ui.DataSelectSpinnerWrapper;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.commons.local_skill.ValidData;

public class BluetoothConnectSkillViewFragment extends SkillViewFragment<BluetoothConnectOperationData> {
    private EditText editText;
    private DataSelectSpinnerWrapper profileSpinner;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skill_operation__bluetooth_connect, container, false);
        editText = view.findViewById(R.id.editText_bluetooth_address);
        profileSpinner = new DataSelectSpinnerWrapper(Objects.requireNonNull(getContext()), (Spinner) view.findViewById(R.id.bluetooth_profile_spinner));

        List<String> options = new ArrayList<>();
        options.add(BluetoothConnectOperationData.A2DP);
        options.add(BluetoothConnectOperationData.HEADSET);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            options.add(BluetoothConnectOperationData.HID_DEVICE);
            options.add(BluetoothConnectOperationData.GATT);
            options.add(BluetoothConnectOperationData.GATT_SERVER);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            options.add(BluetoothConnectOperationData.HEARING_AID);
        }

        profileSpinner.beginInit().setAllowEmpty(false).fillData(options).finalizeInit();
        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull BluetoothConnectOperationData data) {
        editText.setText(data.address);
        profileSpinner.setSelection(data.service);
    }

    @ValidData
    @NonNull
    @Override
    public BluetoothConnectOperationData getData() throws InvalidDataInputException {
        return new BluetoothConnectOperationData(editText.getText().toString(), profileSpinner.getSelection());
    }
}
