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

package ryey.easer.plugins.operation.media_control;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.orhanobut.logger.Logger;

import java.util.List;

import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.operation.OperationLoader;

public class MediaControlLoader extends OperationLoader<MediaControlOperationData> {
    public MediaControlLoader(Context context) {
        super(context);
    }

    @Override
    public boolean load(@ValidData @NonNull MediaControlOperationData data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return handleOnApi21(data);
        } else {
            return fallback(data);
        }
    }

    private boolean fallback(@ValidData @NonNull MediaControlOperationData data) {
        MediaControlOperationData.ControlChoice choice = data.choice;
        emitMediaButton(toKeyCode(choice));
        return true;
    }

    private void emitMediaButton(int keyCode) {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendOrderedBroadcast(intent, null);

        keyEvent = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
        intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendOrderedBroadcast(intent, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean handleOnApi21(@ValidData @NonNull MediaControlOperationData data) {
        int keyCode = toKeyCode(data.choice);
        KeyEvent keyEvent_down = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        KeyEvent keyEvent_up = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
        ComponentName myNotificationListenerComponent = new ComponentName(context, MediaControlHelperNotificationListenerService.class);
        MediaSessionManager mediaSessionManager = ((MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE));
        if (mediaSessionManager == null) {
            Logger.e("MediaSessionManager is null.");
            return false;
        }
        List<MediaController> activeSessions = mediaSessionManager.getActiveSessions(myNotificationListenerComponent);
        if (activeSessions.size() > 0) {
            MediaController mediaController = activeSessions.get(0);
            mediaController.dispatchMediaButtonEvent(keyEvent_down);
            mediaController.dispatchMediaButtonEvent(keyEvent_up);
        }
        return true;
    }

    private static int toKeyCode(MediaControlOperationData.ControlChoice choice) {
        switch (choice) {
            case play_pause:
                return KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
            case play:
                return KeyEvent.KEYCODE_MEDIA_PLAY;
            case pause:
                return KeyEvent.KEYCODE_MEDIA_PAUSE;
            case previous:
                return KeyEvent.KEYCODE_MEDIA_PREVIOUS;
            case next:
                return KeyEvent.KEYCODE_MEDIA_NEXT;
            default:
                throw new IllegalAccessError();
        }
    }
}
