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

package ryey.easer.skills.usource;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;

public abstract class ScannerDialogFragment<T> extends DialogFragment {

    private final List<T> singleDataList = new ArrayList<>();
    private ArrayAdapter<T> dataListAdapter;

    public interface OnPositiveButtonClickedListener<T> {
        boolean onPositiveClicked(@NonNull List<T> singleData);
    }

    @Nullable
    protected GetInitialDataTask<T> getInitialDataTask(List<T> singleDataList, ArrayAdapter<T> dataListAdapter) {
        return null;
    }

    @Nullable
    protected OnPositiveButtonClickedListener<T> positiveButtonClickedListener() {
        return null;
    }

    protected boolean onDialogPositiveButtonClicked(DialogInterface dialog, int id, List<T> singleDataList) {
        OnPositiveButtonClickedListener<T> listener = positiveButtonClickedListener();
        if (listener != null) {
            return listener.onPositiveClicked(singleDataList);
        }
        return false;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_scanner_cell_location, null);

        ListView listView = v.findViewById(R.id.list);
        dataListAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                singleDataList);
        listView.setAdapter(dataListAdapter);

        builder.setView(v);

        if (positiveButtonClickedListener() != null)
            builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (onDialogPositiveButtonClicked(dialog, id, singleDataList))
                        dismiss();
                }
            });

        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        Dialog dialog = builder.create();

        GetInitialDataTask<?> getInitialDataTask = getInitialDataTask(singleDataList, dataListAdapter);
        if (getInitialDataTask != null)
            getInitialDataTask.execute();

        return dialog;
    }

    private static <T> void addData(@NonNull List<T> singleDataList,
                                    @NonNull ArrayAdapter<T> cellLocationDataListAdapter,
                                    @Nullable T data) {
        if (data != null) {
            if (!singleDataList.contains(data)) {
                singleDataList.add(data);
                cellLocationDataListAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void addData(@Nullable T data) {
        addData(singleDataList, dataListAdapter, data);
    }

    protected abstract static class GetInitialDataTask<T> extends AsyncTask<Void, T, Void> {
    }

}
