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

package ryey.easer.plugins.event.dayofweek;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class DayOfWeekEventDataFactory implements EventDataFactory<DayOfWeekEventData> {
    @NonNull
    @Override
    public Class<DayOfWeekEventData> dataClass() {
        return DayOfWeekEventData.class;
    }

    @NonNull
    @Override
    public DayOfWeekEventData emptyData() {
        return new DayOfWeekEventData();
    }

    @ValidData
    @NonNull
    @Override
    public DayOfWeekEventData dummyData() {
        DayOfWeekEventData dummyData = new DayOfWeekEventData();
        dummyData.days.add(2);
        dummyData.days.add(4);
        dummyData.days.add(5);
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public DayOfWeekEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new DayOfWeekEventData(data, format, version);
    }
}
