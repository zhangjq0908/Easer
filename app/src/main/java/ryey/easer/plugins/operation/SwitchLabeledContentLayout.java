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

package ryey.easer.plugins.operation;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Switch;

import ryey.easer.commons.ContentLayout;
import ryey.easer.commons.OperationData;
import ryey.easer.commons.StorageData;

public abstract class SwitchLabeledContentLayout extends ContentLayout.LabeledContentLayout {
    Switch aSwitch;
    public SwitchLabeledContentLayout(Context context) {
        super(context);
        aSwitch = new Switch(context);
        aSwitch.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        addView(aSwitch);
    }

    protected static void setSwitch(Switch sw, Boolean state) {
        sw.setChecked(state == true);
    }

    protected static Boolean fromSwitch(Switch sw) {
        Boolean state;
        if (sw.isChecked()) {
            state = true;
        } else {
            state = false;
        }
        return state;
    }

    @Override
    public void fill(StorageData data) {
        if (data instanceof OperationData) {
            Boolean state = (Boolean) ((OperationData) data).get();
            setSwitch(aSwitch, state);
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    protected Boolean state() {
        return fromSwitch(aSwitch);
    }
}
