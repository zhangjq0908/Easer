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

package ryey.easer.skills.usource.call;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class CallSkillViewFragment extends SkillViewFragment<CallUSourceData> {

    private static final CallUSourceData.CallState []callStates = {
            CallUSourceData.CallState.IDLE,
            CallUSourceData.CallState.RINGING,
            CallUSourceData.CallState.OFFHOOK,
    };
    @IdRes
    private static final int []checkBoxIds = {
            R.id.cb_state_idle,
            R.id.cb_state_ringing,
            R.id.cb_state_ongoing,
    };

    private CheckBox []stateCheckBoxes = new CheckBox[checkBoxIds.length];
    private EditText numberEditText;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skill_usource__call, container, false);

        for (int i = 0; i < checkBoxIds.length; i++) {
            stateCheckBoxes[i] = view.findViewById(checkBoxIds[i]);
        }
        numberEditText = view.findViewById(R.id.et_number);

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull CallUSourceData data) {
        for (int i = 0; i < callStates.length; i++) {
            stateCheckBoxes[i].setChecked(data.callStates.contains(callStates[i]));
        }
        numberEditText.setText(data.number);
    }

    @ValidData
    @NonNull
    @Override
    public CallUSourceData getData() throws InvalidDataInputException {
        ArrayList<CallUSourceData.CallState> states = new ArrayList<>();
        for (int i = 0; i < callStates.length; i++) {
            if (stateCheckBoxes[i].isChecked()) {
                states.add(callStates[i]);
            }
        }
        String number = numberEditText.getText().toString();
        return new CallUSourceData(states, number);
    }
}
