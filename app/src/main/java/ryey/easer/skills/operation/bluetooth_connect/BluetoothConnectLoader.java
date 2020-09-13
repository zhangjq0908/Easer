/*
 * Copyright (c) 2016 - 2018, 2020 Rui Zhao <renyuneyun@gmail.com> and Daniel Landau
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

package ryey.easer.skills.operation.bluetooth_connect;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothHearingAid;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

public class BluetoothConnectLoader extends OperationLoader<BluetoothConnectOperationData> implements BluetoothRequester.Callback {
    private BluetoothAdapter mAdapter;
    private String mAddress;
    private String mService;
    private OnResultCallback mCallback;

    BluetoothConnectLoader(Context context) {
        super(context);
    }

    @Override
    public void _load(@ValidData @NonNull BluetoothConnectOperationData data, @NonNull OnResultCallback callback) {
        mAddress = data.address;
        mService = data.service;
        mCallback = callback;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager != null) {
                mAdapter = bluetoothManager.getAdapter();
                if (mAdapter == null) {
                    Logger.w("no bluetoothAdapter");
                    callback.onResult(false);
                    return;
                }
                new BluetoothRequester(this).request(context, mAdapter, getBluetoothProfile());
            } else {
                Logger.wtf("Couldn't get system bluetooth manager");
                callback.onResult(false);
            }
        } else {
            Logger.wtf("System version lower than min requirement");
            callback.onResult(false);
        }
    }

    private int getBluetoothProfile() {
        switch (mService) {
            case BluetoothConnectOperationData.A2DP:
                return BluetoothProfile.A2DP;
            case BluetoothConnectOperationData.HEADSET:
                return BluetoothProfile.HEADSET;
            case BluetoothConnectOperationData.HID_DEVICE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    return BluetoothProfile.HID_DEVICE;
                break;
            case BluetoothConnectOperationData.HEARING_AID:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    return BluetoothProfile.HEARING_AID;
                break;
            case BluetoothConnectOperationData.GATT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    return BluetoothProfile.GATT;
                break;
            case BluetoothConnectOperationData.GATT_SERVER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    return BluetoothProfile.GATT_SERVER;
                break;
        }
        return -1;
    }

    // From https://github.com/kcoppock/bluetooth-a2dp with modifications
    @Override
    public void onProxyReceived(BluetoothProfile proxy) {
        Method connect = getConnectMethod();
        BluetoothDevice device = findBondedDeviceByAddress(mAdapter, mAddress);
        if (connect == null || device == null) {
            return;
        }

        try {
            connect.setAccessible(true);
            connect.invoke(proxy, device);
            mCallback.onResult(true);
        } catch (InvocationTargetException ignored) {
        } catch (IllegalAccessException ignored) {
        }
    }


    @SuppressLint("PrivateApi")
    private Method getConnectMethod () {
        try {
            switch (mService) {
                case BluetoothConnectOperationData.A2DP:
                    return BluetoothA2dp.class.getDeclaredMethod("connect", BluetoothDevice.class);
                case BluetoothConnectOperationData.HEADSET:
                    return BluetoothHeadset.class.getDeclaredMethod("connect", BluetoothDevice.class);
                case BluetoothConnectOperationData.HID_DEVICE:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        return BluetoothHidDevice.class.getDeclaredMethod("connect", BluetoothDevice.class);
                case BluetoothConnectOperationData.HEARING_AID:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    return BluetoothHearingAid.class.getDeclaredMethod("connect", BluetoothDevice.class);
                case BluetoothConnectOperationData.GATT:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        return BluetoothGatt.class.getDeclaredMethod("connect", BluetoothDevice.class);
                case BluetoothConnectOperationData.GATT_SERVER:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        return BluetoothGattServer.class.getDeclaredMethod("connect", BluetoothDevice.class);
            }
            return null;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    private static BluetoothDevice findBondedDeviceByAddress (BluetoothAdapter adapter, String address) {
        for (BluetoothDevice device : getBondedDevices(adapter)) {
            if (address.matches(device.getAddress())) {
                return device;
            }
        }
        return null;
    }

    private static Set<BluetoothDevice> getBondedDevices (BluetoothAdapter adapter) {
        Set<BluetoothDevice> results = adapter.getBondedDevices();
        if (results == null) {
            results = new HashSet<BluetoothDevice>();
        }
        return results;
    }
}
