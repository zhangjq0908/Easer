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

package ryey.easer.skills.event.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.commons.local_skill.ValidData;

public class WidgetSkillViewFragment extends SkillViewFragment<WidgetEventData> {

    EditText et_id;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skill_event__widget, container, false);

        et_id = view.findViewById(R.id.editText_tag);

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull WidgetEventData data) {
        et_id.setText(data.widgetTag);
    }

    @ValidData
    @NonNull
    @Override
    public WidgetEventData getData() throws InvalidDataInputException {
        String strId = et_id.getText().toString();
        try {
            return new WidgetEventData(strId);
        } catch (NumberFormatException e) {
            throw new InvalidDataInputException("Data is not a valid integer");
        }
    }
}
