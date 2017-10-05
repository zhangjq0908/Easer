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

package ryey.easer.plugins.event.wifi;

import android.content.Context;
import android.widget.EditText;

import ryey.easer.R;
import ryey.easer.commons.plugindef.StorageData;
import ryey.easer.plugins.event.TypedContentLayout;

public class WifiContentLayout extends TypedContentLayout {
    EditText editText;

    {
        expectedDataClass = WifiEventData.class;
    }

    public WifiContentLayout(Context context) {
        super(context);
        setAvailableTypes(new WifiEventData().availableTypes());
        setType(new WifiEventData().type());
        setDesc(context.getString(R.string.event_wificonn));
        inflate(context, R.layout.plugin_event__wifi_connection, this);
        editText = (EditText) findViewById(R.id.wifi_name);
    }

    @Override
    protected void _fill(StorageData data) {
        if (data instanceof WifiEventData) {
            editText.setText((String) data.get());
        }
    }

    @Override
    public StorageData getData() {
        return new WifiEventData(editText.getText().toString(), selectedType());
    }
}
