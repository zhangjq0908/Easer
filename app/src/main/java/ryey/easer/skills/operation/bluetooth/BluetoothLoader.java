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

package ryey.easer.skills.operation.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

public class BluetoothLoader extends OperationLoader<BluetoothOperationData> {
    public BluetoothLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull BluetoothOperationData data) {
        Boolean state = data.get();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter adapter = bluetoothManager.getAdapter();
            if (adapter == null) {
                Logger.w("no BluetoothAdapter");
                return true;
            }
            if (state) {
                return adapter.enable();
            } else {
                return adapter.disable();
            }
        }
        Logger.wtf("System version lower than min requirement");
        return false;
    }
}
