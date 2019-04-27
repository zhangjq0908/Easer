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

package ryey.easer.plugins.condition.connectivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import java.util.Set;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.PluginViewFragment;

public class ConnectivityPluginViewFragment extends PluginViewFragment<ConnectivityConditionData> {
    String []mode_names;
    final int []values = {
            ConnectivityType.TYPE_NOT_CONNECTED,
            ConnectivityType.TYPE_WIFI,
            ConnectivityType.TYPE_MOBILE,
            ConnectivityType.TYPE_ETHERNET,
            ConnectivityType.TYPE_BLUETOOTH,
            ConnectivityType.TYPE_VPN,
    };
    final CheckBox[] checkBoxes = new CheckBox[values.length];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode_names = getResources().getStringArray(R.array.connectivity_type);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i] = new CheckBox(getContext());
            checkBoxes[i].setText(mode_names[i]);
            linearLayout.addView(checkBoxes[i]);
        }
        return linearLayout;
    }

    @Override
    protected void _fill(@ValidData @NonNull ConnectivityConditionData data) {
        Set<Integer> checked_values = data.connectivity_type;
        for (int checked_value : checked_values) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] == checked_value) {
                    checkBoxes[i].setChecked(true);
                    break;
                }
            }
        }
    }

    @ValidData
    @NonNull
    @Override
    public ConnectivityConditionData getData() throws InvalidDataInputException {
        Set<Integer> checked = new ArraySet<>();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                checked.add(values[i]);
            }
        }
        if (checked.size() == 0)
            throw new InvalidDataInputException();
        return new ConnectivityConditionData(checked);
    }
}
