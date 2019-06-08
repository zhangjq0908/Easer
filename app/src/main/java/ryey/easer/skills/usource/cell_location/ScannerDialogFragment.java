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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;

public class ScannerDialogFragment extends DialogFragment {

    private TelephonyManager telephonyManager;
    private CellLocationListener cellLocationListener = new CellLocationListener();

    private List<CellLocationSingleData> singleDataList = new ArrayList<>();
    private ArrayAdapter<CellLocationSingleData> cellLocationDataListAdapter;

    public interface ScannerListener {
        void onPositiveClicked(List<CellLocationSingleData> singleData);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null)
            telephonyManager.listen(cellLocationListener, PhoneStateListener.LISTEN_CELL_LOCATION);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (telephonyManager != null)
            telephonyManager.listen(cellLocationListener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (telephonyManager == null) {
            Toast.makeText(getContext(), R.string.usource_cell_location_no_signal, Toast.LENGTH_SHORT).show();
            dismiss();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_scanner_cell_location, null);

        ListView listView = v.findViewById(R.id.list);
        cellLocationDataListAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                singleDataList);
        listView.setAdapter(cellLocationDataListAdapter);

        builder.setView(v)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((ScannerListener) getTargetFragment()).onPositiveClicked(singleDataList);
                        dismiss();
                    }
                });

        Dialog dialog = builder.create();

        new GetInitialDataTask(telephonyManager, singleDataList, cellLocationDataListAdapter).execute();

        return dialog;
    }

    private static void addData(List<CellLocationSingleData> singleDataList,
                                ArrayAdapter<CellLocationSingleData> cellLocationDataListAdapter,
                                CellLocationSingleData data) {
        if (data != null) {
            if (!singleDataList.contains(data)) {
                singleDataList.add(data);
                cellLocationDataListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void addData(CellLocationSingleData data) {
        addData(singleDataList, cellLocationDataListAdapter, data);
    }

    private class CellLocationListener extends PhoneStateListener {
        @Override
        synchronized public void onCellLocationChanged(CellLocation location) {
            addData(CellLocationSingleData.fromCellLocation(location));
        }
    }

    /**
     * Permission is ensured before using this Fragment
     */
    @SuppressLint("MissingPermission")
    private static class GetInitialDataTask extends AsyncTask<Void, CellLocationSingleData, Void> {

        private TelephonyManager telephonyManager;
        private List<CellLocationSingleData> singleDataList;
        private ArrayAdapter<CellLocationSingleData> adapter;

        private GetInitialDataTask(TelephonyManager telephonyManager,
                                   List<CellLocationSingleData> singleDataList,
                                   ArrayAdapter<CellLocationSingleData> adapter) {
            this.telephonyManager = telephonyManager;
            this.singleDataList = singleDataList;
            this.adapter = adapter;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                for (CellInfo cellInfo : telephonyManager.getAllCellInfo()) {
                    if (cellInfo.isRegistered()) {
                        publishProgress(CellLocationSingleData.fromCellInfo(cellInfo));
                    }
                }
            } else {
                CellLocation cellLocation = telephonyManager.getCellLocation();
                publishProgress(CellLocationSingleData.fromCellLocation(cellLocation));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(CellLocationSingleData... values) {
            addData(singleDataList, adapter, values[0]);
        }
    }

}
