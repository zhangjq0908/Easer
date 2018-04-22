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

package ryey.easer.plugins.operation.volume;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;

import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;

public class VolumeLoader extends OperationLoader<VolumeOperationData> {

    VolumeLoader(Context context) {
        super(context);
    }

    private static void setVolume(AudioManager audioManager, int stream, Integer value) {
        if (value != null)
            audioManager.setStreamVolume(stream, value, 0);
    }

    @Override
    public boolean load(@ValidData @NonNull VolumeOperationData data) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        setVolume(audioManager, AudioManager.STREAM_RING, data.vol_ring);
        setVolume(audioManager, AudioManager.STREAM_MUSIC, data.vol_media);
        setVolume(audioManager, AudioManager.STREAM_ALARM, data.vol_alarm);
        setVolume(audioManager, AudioManager.STREAM_NOTIFICATION, data.vol_notification);
        return true;
    }
}
