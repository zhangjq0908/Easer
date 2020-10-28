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

import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.operationskill.OperationDataFactory;
import ryey.easer.plugin.PluginDataFormat;

class BluetoothConnectOperationDataFactory implements OperationDataFactory<BluetoothConnectOperationData> {
    @NonNull
    @Override
    public Class<BluetoothConnectOperationData> dataClass() {
        return BluetoothConnectOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public BluetoothConnectOperationData dummyData() {
        return new BluetoothConnectOperationData("00:11:22:33:FF:EE",
                BluetoothConnectOperationData.A2DP);
    }

    @ValidData
    @NonNull
    @Override
    public BluetoothConnectOperationData parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        return new BluetoothConnectOperationData(data, format, version);
    }
}
