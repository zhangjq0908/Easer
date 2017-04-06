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

package ryey.easer.plugins.operation.broadcast;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;

import ryey.easer.R;
import ryey.easer.commons.plugindef.ContentLayout;
import ryey.easer.commons.plugindef.StorageData;

public class BroadcastContentLayout extends ContentLayout.LabeledContentLayout {
    EditText mEditText;
    public BroadcastContentLayout(Context context) {
        super(context);
        mEditText = new EditText(context);
        mEditText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        addView(mEditText);
        setDesc(context.getString(R.string.operation_broadcast));
    }

    @Override
    public void fill(StorageData data) {
        String action = (String) data.get();
        mEditText.setText(action);
    }

    @Override
    public StorageData getData() {
        String action = mEditText.getText().toString();
        BroadcastOperationData data = new BroadcastOperationData(action);
        return data;
    }
}
