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

package ryey.easer.plugins.condition.screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;

public class ScreenPluginViewFragment extends PluginViewFragment<ScreenConditionData> {

    private RadioButton rb_screen_on, rb_screen_off, rb_screen_unlocked;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__screen, container, false);
        rb_screen_on = view.findViewById(R.id.radioButton_screen_on);
        rb_screen_off = view.findViewById(R.id.radioButton_screen_off);
        rb_screen_unlocked = view.findViewById(R.id.radioButton_screen_unlocked);
        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull ScreenConditionData data) {
        if (data.screenEvent == ScreenConditionData.ScreenEvent.on)
            rb_screen_on.setChecked(true);
        else if (data.screenEvent == ScreenConditionData.ScreenEvent.off)
            rb_screen_off.setChecked(true);
        else if (data.screenEvent == ScreenConditionData.ScreenEvent.unlocked)
            rb_screen_unlocked.setChecked(true);
    }

    @ValidData
    @NonNull
    @Override
    public ScreenConditionData getData() throws InvalidDataInputException {
        if (rb_screen_on.isChecked())
            return new ScreenConditionData(ScreenConditionData.ScreenEvent.on);
        else if (rb_screen_off.isChecked())
            return new ScreenConditionData(ScreenConditionData.ScreenEvent.off);
        else
            return new ScreenConditionData(ScreenConditionData.ScreenEvent.unlocked);
    }
}
