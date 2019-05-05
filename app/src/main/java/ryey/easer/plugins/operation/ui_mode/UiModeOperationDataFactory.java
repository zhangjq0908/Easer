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

package ryey.easer.plugins.operation.ui_mode;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.commons.local_plugin.operationplugin.OperationDataFactory;
import ryey.easer.plugin.PluginDataFormat;

class UiModeOperationDataFactory implements OperationDataFactory<UiModeOperationData> {
    @NonNull
    @Override
    public Class<UiModeOperationData> dataClass() {
        return UiModeOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public UiModeOperationData dummyData() {
        return new UiModeOperationData(UiModeOperationData.UiMode.car);
    }

    @ValidData
    @NonNull
    @Override
    public UiModeOperationData parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        return new UiModeOperationData(data, format, version);
    }
}
