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
import android.net.Uri;
import android.widget.EditText;
import android.widget.LinearLayout;

import ryey.easer.R;
import ryey.easer.commons.plugindef.ContentLayout;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.plugins.reusable.LeftNRight;

public class BroadcastContentLayout extends ContentLayout {
    EditText m_text_action;
    EditText m_text_category;
    EditText m_text_type;
    EditText m_text_data;
    public BroadcastContentLayout(Context context) {
        super(context);
        LeftNRight lnr;
        lnr = new LeftNRight(context);
        lnr.setLeftText(R.string.obroadcast_l_action);
        addView(lnr);
        m_text_action = lnr.getEditText();
        lnr = new LeftNRight(context);
        lnr.setLeftText(R.string.obroadcast_l_category);
        addView(lnr);
        m_text_category = lnr.getEditText();
        lnr = new LeftNRight(context);
        lnr.setLeftText(R.string.obroadcast_l_type);
        addView(lnr);
        m_text_type = lnr.getEditText();
        lnr = new LeftNRight(context);
        lnr.setLeftText(R.string.obroadcast_l_data);
        addView(lnr);
        m_text_data = lnr.getEditText();
        setDesc(context.getString(R.string.operation_broadcast));
    }

    @Override
    public void fill(StorageData data) {
        IntentData rdata = (IntentData) data.get();
        m_text_action.setText(rdata.action);
        m_text_category.setText(IntentData.categoryToString(rdata.category));
        m_text_type.setText(rdata.type);
        if (rdata.data != null)
            m_text_data.setText(rdata.data.toString());
    }

    @Override
    public StorageData getData() {
        IntentData data = new IntentData();
        data.action = m_text_action.getText().toString();
        data.category = IntentData.stringToCategory(m_text_category.getText().toString());
        data.type = m_text_type.getText().toString();
        data.data = Uri.parse(m_text_data.getText().toString());
        BroadcastOperationData broadcastOperationData = new BroadcastOperationData(data);
        return broadcastOperationData;
    }
}
