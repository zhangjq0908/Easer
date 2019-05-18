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

package ryey.easer.skills.operation.ringer_mode;

import android.os.Build;

enum RingerMode {
    normal,
    vibrate,
    silent,

    //Do Not Disturb mode
    dnd_all,
    dnd_priority,
    dnd_none,
    dnd_alarms,
    ;

    static RingerMode compatible(RingerMode mode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            switch (mode) {
                case dnd_all:
                case dnd_priority:
                case dnd_none:
                case dnd_alarms:
                    mode = silent;
            }
        } else {
            if (mode == silent)
                mode = dnd_priority;
            else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (mode == dnd_alarms)
                    mode = dnd_priority;
            }
        }
        return mode;
    }
}
