/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.orhanobut.logger.Logger;

import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

public class RingerModeLoader extends OperationLoader<RingerModeOperationData> {
    public RingerModeLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull RingerModeOperationData data) {
        RingerMode mode = RingerMode.compatible(data.ringerMode);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mode == RingerMode.vibrate || mode == RingerMode.normal) {
                return amSetMode(audioManager, mode);
            } else {
                return setDoNotDisturbForLollipop(mode);
            }
        } else {
            return amSetMode(audioManager, mode);
        }
    }

    private static boolean amSetMode(AudioManager audioManager, RingerMode mode) {
        int am_mode;
        switch (mode) {
            case silent:
                am_mode = AudioManager.RINGER_MODE_SILENT;
                break;
            case vibrate:
                am_mode = AudioManager.RINGER_MODE_VIBRATE;
                break;
            case normal:
                am_mode = AudioManager.RINGER_MODE_NORMAL;
                break;
            default:
                Logger.w("Running on below Lollipop, but ringer mode %s found", mode);
                return false;
        }
        audioManager.setRingerMode(am_mode);
        if (audioManager.getRingerMode() == am_mode) {
            return true;
        } else {
            Logger.e("not properly set ringer mode :: expected <%s> got <%s>", mode, audioManager.getRingerMode());
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean setDoNotDisturbForLollipop(RingerMode mode) {
        int mode_num;
        if (mode == RingerMode.dnd_all) {
            mode_num = NotificationListenerService.INTERRUPTION_FILTER_ALL;
        } else if (mode == RingerMode.dnd_priority) {
            mode_num = NotificationListenerService.INTERRUPTION_FILTER_PRIORITY;
        } else if (mode == RingerMode.dnd_none) {
            mode_num = NotificationListenerService.INTERRUPTION_FILTER_NONE;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mode == RingerMode.dnd_alarms) {
            mode_num = NotificationListenerService.INTERRUPTION_FILTER_ALARMS;
        } else {
            throw new IllegalStateException("DoNotDisturb mode run out of cases???");
        }
        InterruptionFilterSwitcherService.setInterruptionFilter(context, mode_num);
        return true;
    }
}
