/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.skills.operation.network_transmission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class NetworkTransmissionSkillViewFragment extends SkillViewFragment<NetworkTransmissionOperationData> {

    private RadioButton rb_tcp;
    private RadioButton rb_udp;
    private EditText editText_remote_address;
    private EditText editText_remote_port;
    private EditText editText_data;

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
    protected void _fill(@ValidData @NonNull NetworkTransmissionOperationData data) {
        switch (data.protocol) {
            case tcp:
                rb_tcp.setChecked(true);
                break;
            case udp:
                rb_udp.setChecked(true);
                break;
            default:
                throw new IllegalAccessError();
        }
        editText_remote_address.setText(data.remote_address);
        editText_remote_port.setText(String.valueOf(data.remote_port));
        editText_data.setText(data.data);
    }

    @ValidData
    @NonNull
    @Override
    public NetworkTransmissionOperationData getData() throws InvalidDataInputException {
        NetworkTransmissionOperationData.Protocol protocol;
        if (rb_tcp.isChecked())
            protocol = NetworkTransmissionOperationData.Protocol.tcp;
        else if (rb_udp.isChecked())
            protocol = NetworkTransmissionOperationData.Protocol.udp;
        else
            throw new IllegalStateException("This line ought to be unreachable");
        String remote_address = editText_remote_address.getText().toString();
        int remote_port;
        try {
            remote_port = Integer.valueOf(editText_remote_port.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new InvalidDataInputException("Invalid port");
        }
        String data = editText_data.getText().toString();
        return new NetworkTransmissionOperationData(protocol, remote_address, remote_port, data);
    }
}
