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

package ryey.easer.skills.event.tcp_trip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class TcpTripSkillViewFragment extends SkillViewFragment<TcpTripEventData> {
    private EditText et_rAddr, et_rPort, et_send_data, et_reply_data;
    private CompoundButton b_check_reply;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.plugin_event__tcp_trip, container, false);

        et_rAddr = v.findViewById(R.id.editText_rAddr);
        et_rPort = v.findViewById(R.id.editText_rPort);
        et_send_data = v.findViewById(R.id.editText_send_data);
        b_check_reply = v.findViewById(R.id.checkBox_check_reply);
        et_reply_data = v.findViewById(R.id.editText_reply_data);

        b_check_reply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean newState) {
                et_reply_data.setEnabled(newState);
            }
        });

        b_check_reply.setChecked(true);
        b_check_reply.setChecked(false);

        return v;
    }

    @Override
    protected void _fill(@ValidData @NonNull TcpTripEventData data) {
        et_rAddr.setText(data.rAddr);
        et_rPort.setText(String.valueOf(data.rPort));
        et_send_data.setText(data.send_data);
        b_check_reply.setChecked(data.check_reply);
        if (data.check_reply) {
            et_reply_data.setText(data.reply_data);
        }
    }

    @ValidData
    @NonNull
    @Override
    public TcpTripEventData getData() throws InvalidDataInputException {
        String rAddr = et_rAddr.getText().toString();
        int rPort = Integer.parseInt(et_rPort.getText().toString());
        String send_data = et_send_data.getText().toString();
        boolean check_reply = b_check_reply.isChecked();
        String reply_data = check_reply ? et_reply_data.getText().toString() : null;
        return new TcpTripEventData(rAddr, rPort, send_data, check_reply, reply_data);
    }
}
