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

package ryey.easer.skills.event.broadcast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class BroadcastSkillViewFragment extends SkillViewFragment<BroadcastEventData> {
    private EditText editText_action;
    private EditText editText_category;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__broadcast, container, false);
        editText_action = view.findViewById(R.id.editText_action);
        editText_category = view.findViewById(R.id.editText_category);

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull BroadcastEventData data) {
        ReceiverSideIntentData intentData = data.intentData;
        editText_action.setText(Utils.StringCollectionToString(intentData.action, false));
        editText_category.setText(Utils.StringCollectionToString(intentData.category, false));
    }

    @ValidData
    @NonNull
    @Override
    public BroadcastEventData getData() throws InvalidDataInputException {
        ReceiverSideIntentData intentData = new ReceiverSideIntentData();
        intentData.action = Utils.stringToStringList(editText_action.getText().toString());
        intentData.category = Utils.stringToStringList(editText_category.getText().toString());
        return new BroadcastEventData(intentData);
    }
}
