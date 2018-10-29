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

package ryey.easer.plugins.condition.wifi_enabled;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.PluginViewFragment;

public class WifiEnabledPluginViewFragment extends PluginViewFragment<WifiEnabledConditionData> {

    RadioButton radioButton_enabled, radioButton_disabled;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_condition_wifi_enabled, container, false);

        radioButton_enabled = view.findViewById(R.id.radioButton_yes);
        radioButton_disabled = view.findViewById(R.id.radioButton_no);

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull WifiEnabledConditionData data) {
        if (data.enabled) {
            radioButton_enabled.setChecked(true);
        } else {
            radioButton_disabled.setChecked(true);
        }
    }

    @ValidData
    @NonNull
    @Override
    public WifiEnabledConditionData getData() throws InvalidDataInputException {
        boolean enabled = radioButton_enabled.isChecked();
        return new WifiEnabledConditionData(enabled);
    }
}
