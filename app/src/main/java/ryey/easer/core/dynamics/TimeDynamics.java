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

package ryey.easer.core.dynamics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ryey.easer.R;

class TimeDynamics implements CoreDynamicsInterface {

    private static final SimpleDateFormat sdf_time = new SimpleDateFormat("HH-mm-ss", Locale.US);

    @Override
    public String id() {
        return "ryey.easer.core.dynamics.time";
    }

    @Override
    public int nameRes() {
        return R.string.dynamics_time;
    }

    @Override
    public String invoke(@NonNull Context context, @NonNull Bundle extras) {
        Date now = Calendar.getInstance().getTime();
        return sdf_time.format(now);
    }
}
