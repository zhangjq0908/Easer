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

package ryey.easer.plugins.operation.ringer_mode;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.IBinder;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;

public class RingerModeLoader extends OperationLoader {
    public RingerModeLoader(Context context) {
        super(context);
    }

    @Override
    public boolean _load(OperationData data) {
        Integer mode = (Integer) data.get();
        if (mode == null)
            return true;
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (mode == AudioManager.RINGER_MODE_SILENT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioManager.setRingerMode(mode);
            return setSilentForLollipop();
        } else {
            audioManager.setRingerMode(mode);
            if (audioManager.getRingerMode() == mode) {
                return true;
            } else {
                Logger.e("not properly set ringer mode :: expected <%s> got <%s>", mode, audioManager.getRingerMode());
                return false;
            }
        }
    }

    private boolean setSilentForLollipop() {
        Intent intent = new Intent(context, DumbNotificationListenerService.class);
        final ConditionVariable cv = new ConditionVariable();
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ((DumbNotificationListenerService.DumbBinder) service).setSilent();
                cv.open();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        if (context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            cv.block();
            context.unbindService(connection);
            return true;
        } else
            return false;
    }
}
