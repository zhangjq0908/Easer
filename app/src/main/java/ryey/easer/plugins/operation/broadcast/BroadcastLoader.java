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

package ryey.easer.plugins.operation.broadcast;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ryey.easer.commons.OperationData;
import ryey.easer.commons.OperationLoader;

public class BroadcastLoader extends OperationLoader {
    public BroadcastLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(OperationData data) {
        Log.d(getClass().getSimpleName(), "load");
        String action = (String) data.get();
        if (action != null) {
            Intent intent = new Intent(action);
            context.sendBroadcast(intent);
            Log.d(getClass().getSimpleName(), String.format("broadcast (%s) has been sent", action));
            return true;
        }
        return false;
    }
}
