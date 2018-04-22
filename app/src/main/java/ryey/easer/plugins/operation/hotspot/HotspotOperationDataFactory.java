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

package ryey.easer.plugins.operation.hotspot;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class HotspotOperationDataFactory implements OperationDataFactory<HotspotOperationData> {
    @NonNull
    @Override
    public Class<HotspotOperationData> dataClass() {
        return HotspotOperationData.class;
    }

    @NonNull
    @Override
    public HotspotOperationData emptyData() {
        return new HotspotOperationData();
    }

    @ValidData
    @NonNull
    @Override
    public HotspotOperationData dummyData() {
        HotspotOperationData dummyData = new HotspotOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public HotspotOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new HotspotOperationData(data, format, version);
    }
}
