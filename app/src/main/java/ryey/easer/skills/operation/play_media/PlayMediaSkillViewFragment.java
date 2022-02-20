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

package ryey.easer.skills.operation.play_media;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class PlayMediaSkillViewFragment extends SkillViewFragment<PlayMediaOperationData> {
    private EditText et_file;
    private CheckBox cb_loop;
    private TextInputLayout til_repeat_times;
    private EditText t_repeat_times;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.skill_operation__play_media, container, false);
        et_file = v.findViewById(R.id.editText_file);
        cb_loop = v.findViewById(R.id.checkBox_loop);
        cb_loop.setOnCheckedChangeListener((buttonView, isChecked) ->
                til_repeat_times.setVisibility(isChecked ? View.VISIBLE : View.GONE));
        til_repeat_times = v.findViewById(R.id.text_repeat_times);
        t_repeat_times = til_repeat_times.getEditText();
        return v;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void _fill(@ValidData @NonNull PlayMediaOperationData data) {
        et_file.setText(data.filePath);
        cb_loop.setChecked(data.loop);
        if (data.loop) {
            t_repeat_times.setText(Integer.toString(data.repeat_times));
        }
    }

    @ValidData
    @NonNull
    @Override
    public PlayMediaOperationData getData() throws InvalidDataInputException {
        boolean loop = cb_loop.isChecked();
        int repeat_times = loop ? Integer.parseInt(t_repeat_times.getText().toString()) : 0;
        return new PlayMediaOperationData(et_file.getText().toString(), loop, repeat_times);
    }
}
