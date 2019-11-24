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
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ryey.easer.R;
import ryey.easer.skills.usource.ScannerDialogFragment;

public class CellLocationScannerDialogFragment extends ScannerDialogFragment<CellLocationSingleData> {

    private TelephonyManager telephonyManager;
    private final CellLocationListener cellLocationListener = new CellLocationListener();

    @Override
    public void onAttach(@NonNull Context context) {
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
    protected ScannerDialogFragment.GetInitialDataTask<CellLocationSingleData> getInitialDataTask(List<CellLocationSingleData> singleDataList, ArrayAdapter<CellLocationSingleData> dataListAdapter) {
        return new GetInitialCellLocationDataTask(telephonyManager, singleDataList, dataListAdapter);
    }

    @Nullable
    @Override
    protected OnPositiveButtonClickedListener<CellLocationSingleData> positiveButtonClickedListener() {
        return (OnPositiveButtonClickedListener<CellLocationSingleData>) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (telephonyManager == null) {
            Toast.makeText(getContext(), R.string.usource_cell_location_no_signal, Toast.LENGTH_SHORT).show();
            dismiss();
        }

        return super.onCreateDialog(savedInstanceState);
    }

    private static void addData(@NonNull List<CellLocationSingleData> singleDataList,
                                @NonNull ArrayAdapter<CellLocationSingleData> cellLocationDataListAdapter,
                                @Nullable CellLocationSingleData data) {
        if (data != null) {
            if (!singleDataList.contains(data)) {
                singleDataList.add(data);
                cellLocationDataListAdapter.notifyDataSetChanged();
            }
        }
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
    private static class GetInitialCellLocationDataTask extends GetInitialDataTask<CellLocationSingleData> {

        private TelephonyManager telephonyManager;
        private List<CellLocationSingleData> singleDataList;
        private ArrayAdapter<CellLocationSingleData> adapter;

        private GetInitialCellLocationDataTask(TelephonyManager telephonyManager,
                                               List<CellLocationSingleData> singleDataList,
                                               ArrayAdapter<CellLocationSingleData> adapter) {
            this.telephonyManager = telephonyManager;
            this.singleDataList = singleDataList;
            this.adapter = adapter;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
                if (cellInfoList != null) {
                    for (CellInfo cellInfo : cellInfoList) {
                        if (cellInfo.isRegistered()) {
                            publishProgress(CellLocationSingleData.fromCellInfo(cellInfo));
                        }
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
