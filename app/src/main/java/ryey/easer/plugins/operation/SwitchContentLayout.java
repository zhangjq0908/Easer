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
import android.support.annotation.NonNull;
import android.widget.Switch;

import ryey.easer.R;
import ryey.easer.commons.plugindef.ContentLayout;
import ryey.easer.commons.plugindef.StorageData;

public abstract class SwitchContentLayout extends ContentLayout {
    Switch aSwitch;

    {
        expectedDataClass = BooleanOperationData.class;
    }

    public SwitchContentLayout(Context context) {
        super(context);
        inflate(context, R.layout.plugin_reusable__switch, this);
        aSwitch = (Switch) findViewById(R.id.plugin_reusable__switch);
    }

    protected static void setSwitch(@NonNull Switch sw, Boolean state) {
        sw.setChecked(state);
    }

    @NonNull
    protected static Boolean fromSwitch(@NonNull Switch sw) {
        return sw.isChecked();
    }

    @Override
    protected void _fill(StorageData data) {
        if (data instanceof BooleanOperationData) {
            Boolean state = (Boolean) data.get();
            setSwitch(aSwitch, state);
        }
    }

    protected Boolean state() {
        return fromSwitch(aSwitch);
    }
}
