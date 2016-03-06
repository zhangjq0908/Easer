/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.event.date;

import java.util.Calendar;

import ryey.easer.commons.EventData;

public class DateEventData implements EventData {
    Calendar date = null;

    public DateEventData() {}

    public DateEventData(Calendar date) {
        this.date = date;
    }

    @Override
    public Object get() {
        return date;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof Calendar) {
            date = (Calendar) obj;
        } else {
            throw new RuntimeException("illegal data type");
        }
    }

    @Override
    public boolean isValid() {
        if (date == null)
            return false;
        return true;
    }
}
