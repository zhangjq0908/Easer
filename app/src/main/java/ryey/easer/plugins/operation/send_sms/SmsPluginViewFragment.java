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

package ryey.easer.plugins.operation.send_sms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.StorageData;

public class SmsPluginViewFragment extends PluginViewFragment {
    EditText et_destination, et_content;

    {
        setDesc(R.string.operation_send_sms);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__send_sms, container, false);
        et_destination = view.findViewById(R.id.editText_destination);
        et_content = view.findViewById(R.id.editText_content);
        return view;
    }

    @Override
    protected void _fill(@NonNull StorageData data) {
        Sms sms = (Sms) data.get();
        et_destination.setText(sms.destination);
        et_content.setText(sms.content);
    }

    @NonNull
    @Override
    public StorageData getData() throws InvalidDataInputException {
        Sms sms = new Sms();
        sms.destination = et_destination.getText().toString();
        sms.content = et_content.getText().toString();
        return new SmsOperationData(sms);
    }
}
