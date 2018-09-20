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

package ryey.easer.plugins.operation.state_control;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.support.annotation.NonNull;

import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.core.EHService;
import ryey.easer.plugins.operation.OperationLoader;

public class StateControlLoader extends OperationLoader<StateControlOperationData> {

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (EHService.EHBinder) iBinder;
            cv.open();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            cv.close();
            binder = null;
        }
    };
    private ConditionVariable cv = new ConditionVariable();

    private EHService.EHBinder binder;

    StateControlLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull StateControlOperationData data) {
        cv.close();
        Intent intent = new Intent(context, EHService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        cv.block();
        return binder.setLotusStatus(data.scriptName, data.newStatus);
    }
}
