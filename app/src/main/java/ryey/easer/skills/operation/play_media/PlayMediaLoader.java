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

package ryey.easer.skills.operation.play_media;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

public class PlayMediaLoader extends OperationLoader<PlayMediaOperationData> {
    PlayMediaLoader(Context context) {
        super(context);
    }

    @Override
    public void _load(@ValidData @NonNull PlayMediaOperationData data, @NonNull OnResultCallback callback) {
        MediaPlayer mp = new MediaPlayer();
        AtomicInteger repeated_times = new AtomicInteger(0);  // Must be (effectively) final to be used in lambda
        try {
            mp.setDataSource(data.filePath);
            mp.setLooping(false);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(mp1 -> {
                if (data.loop && repeated_times.addAndGet(1) < data.repeat_times) {
                    mp1.start();
                } else {
                    mp1.release();
                }
            });
            callback.onResult(true);
        } catch (IOException e) {
            e.printStackTrace();
            callback.onResult(false);
        }
    }
}
