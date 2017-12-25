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

package ryey.easer.plugins.event.nfc_tag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import ryey.easer.R;
import ryey.easer.commons.plugindef.InvalidDataInputException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.ValidData;

public class NfcTagPluginViewFragment extends PluginViewFragment<NfcTagEventData> {

    private static final int REQCODE_WAIT_FOR_TAG = 120;

    private EditText editText;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQCODE_WAIT_FOR_TAG && resultCode == Activity.RESULT_OK) {
            Logger.d("got expected result. setting data");
            byte[] tag_id = data.getByteArrayExtra(WaitForNfcActivity.EXTRA_ID);
            editText.setText(NfcTagEventData.byteArray2hexString(tag_id));
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__nfc_tag, container, false);

        editText = view.findViewById(R.id.editText_tag_id);
        view.findViewById(R.id.button_wait_for_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WaitForNfcActivity.class);
                startActivityForResult(intent, REQCODE_WAIT_FOR_TAG);
            }
        });

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull NfcTagEventData data) {
        editText.setText(data.toString());
    }

    @ValidData
    @NonNull
    @Override
    public NfcTagEventData getData() throws InvalidDataInputException {
        return new NfcTagEventData(editText.getText().toString());
    }
}
