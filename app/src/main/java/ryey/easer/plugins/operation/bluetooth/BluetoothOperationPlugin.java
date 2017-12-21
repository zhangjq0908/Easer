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

package ryey.easer.plugins.operation.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.commons.plugindef.operationplugin.PrivilegeUsage;
import ryey.easer.plugins.reusable.PluginHelper;

public class BluetoothOperationPlugin implements OperationPlugin {

    @NonNull
    @Override
    public String id() {
        return "bluetooth";
    }

    @Override
    public int name() {
        return R.string.operation_bluetooth;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null;
    }

    @NonNull
    @Override
    public PrivilegeUsage privilege() {
        return PrivilegeUsage.no_root;
    }

    @Override
    public int maxExistence() {
        return 1;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return PluginHelper.checkPermission(context,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        boolean can_access_bluetooth = PluginHelper.checkPermission(activity, Manifest.permission.BLUETOOTH);
        boolean can_bluetooth_admin = PluginHelper.checkPermission(activity, Manifest.permission.BLUETOOTH_ADMIN);
        if (!can_access_bluetooth && !can_bluetooth_admin) {
            PluginHelper.requestPermission(activity, requestCode,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN);
        } else if (!can_access_bluetooth) {
            PluginHelper.requestPermission(activity, requestCode,
                    Manifest.permission.BLUETOOTH);
        } else {
            PluginHelper.requestPermission(activity, requestCode,
                    Manifest.permission.BLUETOOTH_ADMIN);
        }
    }

    @NonNull
    @Override
    public OperationDataFactory dataFactory() {
        return new OperationDataFactory() {
            @NonNull
            @Override
            public Class<? extends OperationData> dataClass() {
                return BluetoothOperationData.class;
            }

            @NonNull
            @Override
            public OperationData emptyData() {
                return new BluetoothOperationData();
            }

            @NonNull
            @Override
            public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
                return new BluetoothOperationData(data, format, version);
            }
        };

    }

    @NonNull
    @Override
    public PluginViewFragment view() {
        return new BluetoothPluginViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader loader(@NonNull Context context) {
        return new BluetoothLoader(context);
    }
}
