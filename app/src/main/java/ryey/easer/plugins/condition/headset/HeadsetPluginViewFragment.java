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

package ryey.easer.plugins.condition.headset;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.PluginViewFragment;

public class HeadsetPluginViewFragment extends PluginViewFragment<HeadsetConditionData> {
    private static final int[] ids_hs_state = {
            R.id.radioButton_plug_in,
            R.id.radioButton_plug_out,
    };
    private static final int[] ids_hs_type = {
            R.id.radioButton_micro_true,
            R.id.radioButton_micro_false,
            R.id.radioButton_micro_any,
    };

    private RadioButton[] radioButtons_hs_state = new RadioButton[ids_hs_state.length];
    private RadioButton[] radioButtons_hs_type = new RadioButton[ids_hs_type.length];

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.plugin_event__headset, container, false);
        for (int i = 0; i < ids_hs_state.length; i++) {
            radioButtons_hs_state[i] = v.findViewById(ids_hs_state[i]);
        }
        for (int i = 0; i < ids_hs_type.length; i++) {
            radioButtons_hs_type[i] = v.findViewById(ids_hs_type[i]);
        }
        return v;
    }

    @Override
    protected void _fill(@ValidData @NonNull HeadsetConditionData data) {
        radioButtons_hs_state[data.hs_state.ordinal()].setChecked(true);
        radioButtons_hs_type[data.hs_type.ordinal()].setChecked(true);
    }

    @ValidData
    @NonNull
    @Override
    public HeadsetConditionData getData() throws InvalidDataInputException {
        return new HeadsetConditionData(
                HeadsetConditionData.HeadsetState.values()[Utils.checkedIndexFirst(radioButtons_hs_state)],
                HeadsetConditionData.HeadsetType.values()[Utils.checkedIndexFirst(radioButtons_hs_type)]
        );
    }
}
