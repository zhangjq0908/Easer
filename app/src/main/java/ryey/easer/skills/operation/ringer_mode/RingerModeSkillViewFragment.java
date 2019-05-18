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

package ryey.easer.skills.operation.ringer_mode;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class RingerModeSkillViewFragment extends SkillViewFragment<RingerModeOperationData> {

    RadioButton rb_normal, rb_vibrate, rb_silent_dnd;
    ViewGroup container_dnd;
    RadioButton rb_dnd_all, rb_dnd_priority, rb_dnd_none, rb_dnd_alarms;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__ringer_mode, container, false);

        rb_normal = view.findViewById(R.id.radioButton_normal);
        rb_vibrate = view.findViewById(R.id.radioButton_vibrate);
        rb_silent_dnd = view.findViewById(R.id.radioButton_silent_dnd);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            container_dnd = view.findViewById(R.id.container_dnd);
            rb_silent_dnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    container_dnd.setVisibility(b ? View.VISIBLE : View.GONE);
                }
            });
            rb_dnd_all = view.findViewById(R.id.radioButton_dnd_all);
            rb_dnd_priority = view.findViewById(R.id.radioButton_dnd_priority);
            rb_dnd_none = view.findViewById(R.id.radioButton_dnd_none);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rb_dnd_alarms = view.findViewById(R.id.radioButton_dnd_alarms);
            }
        }

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull RingerModeOperationData data) {
        RingerMode mode = RingerMode.compatible(data.ringerMode);
        if (mode == RingerMode.normal)
            rb_normal.setChecked(true);
        else if (mode == RingerMode.vibrate)
            rb_vibrate.setChecked(true);
        else if (mode == RingerMode.silent)
            rb_silent_dnd.setChecked(true);
        else {
            rb_silent_dnd.setChecked(true);
            if (mode == RingerMode.dnd_all)
                rb_dnd_all.setChecked(true);
            else if (mode == RingerMode.dnd_priority)
                rb_dnd_priority.setChecked(true);
            else if (mode == RingerMode.dnd_none)
                rb_dnd_none.setChecked(true);
            else
                rb_dnd_alarms.setChecked(true);
        }
    }

    @ValidData
    @NonNull
    @Override
    public RingerModeOperationData getData() throws InvalidDataInputException {
        RingerMode mode = null;
        if (rb_normal.isChecked())
            mode = RingerMode.normal;
        else if (rb_vibrate.isChecked())
            mode = RingerMode.vibrate;
        else if (rb_silent_dnd.isChecked()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                mode = RingerMode.silent;
            else {
                if (rb_dnd_all.isChecked())
                    mode = RingerMode.dnd_all;
                else if (rb_dnd_priority.isChecked())
                    mode = RingerMode.dnd_priority;
                else if (rb_dnd_none.isChecked())
                    mode = RingerMode.dnd_none;
                else if (rb_dnd_alarms.isChecked())
                    mode = RingerMode.dnd_alarms;
                else
                    Utils.panic("Select RingerMode run out of cases");
            }
        } else
            Utils.panic("Select RingerMode run out of cases");
        return new RingerModeOperationData(mode);
    }

}
