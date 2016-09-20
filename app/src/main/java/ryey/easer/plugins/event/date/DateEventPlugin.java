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

package ryey.easer.plugins.event.date;

import android.content.Context;

import ryey.easer.commons.AbstractSlot;
import ryey.easer.commons.ContentLayout;
import ryey.easer.commons.EventData;
import ryey.easer.commons.EventPlugin;

public class DateEventPlugin implements EventPlugin {

    static String pname() {
        return "date";
    }

    @Override
    public String name() {
        return pname();
    }

    @Override
    public EventData data() {
        return new DateEventData();
    }

    @Override
    public ContentLayout view(Context context) {
        return new DateContentLayout(context);
    }

    @Override
    public AbstractSlot slot(Context context) {
        return new DateSlot(context);
    }
}
