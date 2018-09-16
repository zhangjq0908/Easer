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

package ryey.easer.plugins.operation.brightness;

import android.support.annotation.NonNull;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class BrightnessOperationDataFactory implements OperationDataFactory<BrightnessOperationData> {
    @NonNull
    @Override
    public Class<BrightnessOperationData> dataClass() {
        return BrightnessOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public BrightnessOperationData dummyData() {
        return new BrightnessOperationData(3);
    }

    @ValidData
    @NonNull
    @Override
    public BrightnessOperationData parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        return new BrightnessOperationData(data, format, version);
    }
}
