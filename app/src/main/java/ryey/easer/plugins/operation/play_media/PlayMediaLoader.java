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

package ryey.easer.plugins.operation.play_media;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import java.io.IOException;

import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.operation.OperationLoader;

public class PlayMediaLoader extends OperationLoader<PlayMediaOperationData> {
    PlayMediaLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull PlayMediaOperationData data) {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(data.filePath);
            mp.prepare();
            mp.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
