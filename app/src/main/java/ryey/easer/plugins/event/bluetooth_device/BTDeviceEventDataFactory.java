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

package ryey.easer.plugins.event.bluetooth_device;

import android.support.annotation.NonNull;

import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.PluginDataFormat;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class BTDeviceEventDataFactory implements EventDataFactory<BTDeviceEventData> {
    @NonNull
    @Override
    public Class<BTDeviceEventData> dataClass() {
        return BTDeviceEventData.class;
    }

    @ValidData
    @NonNull
    @Override
    public BTDeviceEventData dummyData() {
        BTDeviceEventData dummyData = new BTDeviceEventData(new String[]{"device1", "dev2"});
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public BTDeviceEventData parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        return new BTDeviceEventData(data, format, version);
    }
}
