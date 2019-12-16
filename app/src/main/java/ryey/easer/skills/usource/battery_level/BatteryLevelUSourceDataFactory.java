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

package ryey.easer.skills.usource.battery_level;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.plugin.PluginDataFormat;

class BatteryLevelUSourceDataFactory implements USourceDataFactory<BatteryLevelUSourceData> {
    @NonNull
    @Override
    public Class<BatteryLevelUSourceData> dataClass() {
        return BatteryLevelUSourceData.class;
    }

    @ValidData
    @NonNull
    @Override
    public BatteryLevelUSourceData dummyData() {
        return new BatteryLevelUSourceData(BatteryLevelUSourceData.Type.custom,
                new BatteryLevelUSourceData.CustomLevel(20, false));
    }

    @ValidData
    @NonNull
    @Override
    public BatteryLevelUSourceData parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        return new BatteryLevelUSourceData(data, format, version);
    }
}
