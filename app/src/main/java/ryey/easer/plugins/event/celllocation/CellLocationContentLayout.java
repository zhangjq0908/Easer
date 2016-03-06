/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.event.celllocation;

import android.content.Context;
import android.widget.EditText;

import ryey.easer.R;
import ryey.easer.commons.StorageData;
import ryey.easer.commons.SwitchItemLayout;

public class CellLocationContentLayout extends SwitchItemLayout.LabeledContentLayout {
    EditText editText;

    public CellLocationContentLayout(Context context) {
        super(context);
        setDesc(context.getString(R.string.event_celllocation));
        editText = new EditText(context);
        addView(editText);
    }

    @Override
    public void fill(StorageData data) {
        if (data instanceof CellLocationEventData) {
            editText.setText((String) data.get());
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public StorageData getData() {
        return new CellLocationEventData(editText.getText().toString());
    }
}
