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

package ryey.easer.skills.operation.media_control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class MediaControlSkillViewFragment extends SkillViewFragment<MediaControlOperationData> {

    private RadioButton radioButton_play_pause;
    private RadioButton radioButton_play;
    private RadioButton radioButton_pause;
    private RadioButton radioButton_previous;
    private RadioButton radioButton_next;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__media_control, container, false);
        radioButton_play_pause = view.findViewById(R.id.radioButton_play_pause);
        radioButton_play = view.findViewById(R.id.radioButton_play);
        radioButton_pause = view.findViewById(R.id.radioButton_pause);
        radioButton_previous = view.findViewById(R.id.radioButton_previous);
        radioButton_next = view.findViewById(R.id.radioButton_next);

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull MediaControlOperationData data) {
        MediaControlOperationData.ControlChoice choice = data.choice;
        switch (choice) {
            case play_pause:
                radioButton_play_pause.setChecked(true);
                break;
            case play:
                radioButton_play.setChecked(true);
                break;
            case pause:
                radioButton_pause.setChecked(true);
                break;
            case previous:
                radioButton_previous.setChecked(true);
                break;
            case next:
                radioButton_next.setChecked(true);
                break;
        }
    }

    @ValidData
    @NonNull
    @Override
    public MediaControlOperationData getData() throws InvalidDataInputException {
        MediaControlOperationData.ControlChoice choice = null;
        if (radioButton_play_pause.isChecked()) {
            choice = MediaControlOperationData.ControlChoice.play_pause;
        } else if (radioButton_play.isChecked()) {
            choice = MediaControlOperationData.ControlChoice.play;
        } else if (radioButton_pause.isChecked()) {
            choice = MediaControlOperationData.ControlChoice.pause;
        } else if (radioButton_previous.isChecked()) {
            choice = MediaControlOperationData.ControlChoice.previous;
        } else if (radioButton_next.isChecked()) {
            choice = MediaControlOperationData.ControlChoice.next;
        }
        if (choice == null)
            throw new InvalidDataInputException();
        return new MediaControlOperationData(choice);
    }
}
