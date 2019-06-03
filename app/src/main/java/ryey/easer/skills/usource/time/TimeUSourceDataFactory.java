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

package ryey.easer.skills.usource.time;

import androidx.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.plugin.PluginDataFormat;

class TimeUSourceDataFactory implements USourceDataFactory<TimeUSourceData> {
    @NonNull
    @Override
    public Class<TimeUSourceData> dataClass() {
        return TimeUSourceData.class;
    }

    @ValidData
    @NonNull
    @Override
    public TimeUSourceData dummyData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 23);
        return new TimeUSourceData(calendar, TimeUSourceData.Rel.after);
    }

    @ValidData
    @NonNull
    @Override
    public TimeUSourceData parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        return new TimeUSourceData(data, format, version);
    }
}
