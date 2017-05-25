/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.operation.ringer_mode;

import android.content.Context;
import android.media.AudioManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ryey.easer.R;
import ryey.easer.commons.plugindef.ContentLayout;
import ryey.easer.commons.plugindef.StorageData;

public class RingerModeContentLayout extends ContentLayout {
    String []mode_names = getResources().getStringArray(R.array.ringer_mode);
    int []values = {
            AudioManager.RINGER_MODE_SILENT,
            AudioManager.RINGER_MODE_VIBRATE,
            AudioManager.RINGER_MODE_NORMAL
    };
    RadioButton []radioButtons = new RadioButton[values.length];

    int checked_item = -1;

    public RingerModeContentLayout(Context context) {
        super(context);
        setDesc(context.getString(R.string.operation_ringer_mode));
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(HORIZONTAL);
        addView(radioGroup);
        OnClickListener radioButtonOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < radioButtons.length; i++) {
                    if (v == radioButtons[i]) {
                        checked_item = i;
                        break;
                    }
                }
            }
        };
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i] = new RadioButton(context);
            radioButtons[i].setText(mode_names[i]);
            radioButtons[i].setOnClickListener(radioButtonOnClickListener);
            radioGroup.addView(radioButtons[i]);
        }
    }

    @Override
    public void fill(StorageData data) {
        Integer item = (Integer) data.get();
        for (int i = 0; i < radioButtons.length; i++) {
            if (item == values[i]) {
                radioButtons[i].setChecked(true);
                break;
            }
        }
    }

    @Override
    public StorageData getData() {
        return new RingerModeOperationData(checked_item);
    }
}
