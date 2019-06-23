/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.skills.usource.cell_location;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.SkillViewFragment;

public class CellLocationSkillViewFragment extends SkillViewFragment<CellLocationUSourceData> implements ScannerDialogFragment.ScannerListener {
    private EditText editText;

    private static final int DIALOG_FRAGMENT = 1;

    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_usource__cell_location, container, false);

        editText = view.findViewById(R.id.location_text);
        view.findViewById(R.id.location_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SkillUtils.checkPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION))
                    return;

                DialogFragment dialogFrag = new ScannerDialogFragment();
                dialogFrag.setTargetFragment(CellLocationSkillViewFragment.this, DIALOG_FRAGMENT);
                dialogFrag.show(getFragmentManager(), "dialog");

            }
        });

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull CellLocationUSourceData data) {
        editText.setText(data.toString());
    }

    @ValidData
    @NonNull
    @Override
    public CellLocationUSourceData getData() throws InvalidDataInputException {
        CellLocationUSourceData data = new CellLocationUSourceData(editText.getText().toString().split("\n"));
        if (data.isValid())
            return data;
        throw new InvalidDataInputException();
    }

    @Override
    public void onPositiveClicked(List<CellLocationSingleData> singleDataList) {
        String display_str = editText.getText().toString();
        StringBuilder stringBuilder = new StringBuilder(display_str);
        for (CellLocationSingleData singleData : singleDataList) {
            stringBuilder.append('\n')
                    .append(singleData.toString());
        }
        if (stringBuilder.charAt(0) == '\n')
            stringBuilder.deleteCharAt(0);
        editText.setText(stringBuilder.toString());
    }
}
