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

package ryey.easer.plugins.operation.command;

import android.content.Context;
import android.widget.EditText;

import ryey.easer.R;
import ryey.easer.commons.plugindef.ContentLayout;
import ryey.easer.commons.plugindef.StorageData;

public class CommandContentLayout extends ContentLayout {
    EditText editText_command;

    {
        expectedDataClass = CommandOperationData.class;
    }

    public CommandContentLayout(Context context) {
        super(context);
        setDesc(context.getString(R.string.operation_command));
        inflate(context, R.layout.plugin_operation__command, this);
        editText_command = (EditText) findViewById(R.id.command);
    }

    @Override
    protected void _fill(StorageData data) {
        String command = (String) data.get();
        editText_command.setText(command);
    }

    @Override
    public StorageData getData() {
        return new CommandOperationData(editText_command.getText().toString());
    }
}
