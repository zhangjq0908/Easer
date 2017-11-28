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

package ryey.easer.plugins.operation.network_transmission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.StorageData;

public class NetworkTransmissionPluginViewFragment extends PluginViewFragment {

    RadioButton rb_tcp, rb_udp;
    EditText editText_remote_address, editText_remote_port;
    EditText editText_data;

    {
        setDesc(R.string.operation_network_transmission);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__network_transmission, container, false);
        rb_tcp = view.findViewById(R.id.radioButton_tcp);
        rb_udp = view.findViewById(R.id.radioButton_udp);
        editText_remote_address = view.findViewById(R.id.editText_remote_address);
        editText_remote_port = view.findViewById(R.id.editText_port);
        editText_data = view.findViewById(R.id.editText_data);
        return view;
    }

    @Override
    protected void _fill(@NonNull StorageData data) {
        TransmissionData tdata = (TransmissionData) data.get();
        switch (tdata.protocol) {
            case tcp:
                rb_tcp.setChecked(true);
                break;
            case udp:
                rb_udp.setChecked(true);
                break;
            default:
                throw new IllegalAccessError();
        }
        editText_remote_address.setText(tdata.remote_address);
        editText_remote_port.setText(String.valueOf(tdata.remote_port));
        editText_data.setText(tdata.data);
    }

    @NonNull
    @Override
    public StorageData getData() throws InvalidDataInputException {
        TransmissionData tdata = new TransmissionData();
        if (rb_tcp.isChecked())
            tdata.protocol = TransmissionData.Protocol.tcp;
        else if (rb_udp.isChecked())
            tdata.protocol = TransmissionData.Protocol.udp;
        tdata.remote_address = editText_remote_address.getText().toString();
        try {
            tdata.remote_port = Integer.valueOf(editText_remote_port.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        tdata.data = editText_data.getText().toString();
        return new NetworkTransmissionOperationData(tdata);
    }
}
