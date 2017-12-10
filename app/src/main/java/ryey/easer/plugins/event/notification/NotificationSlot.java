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

package ryey.easer.plugins.event.notification;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.SelfNotifiableSlot;

public class NotificationSlot extends SelfNotifiableSlot {
    private NotificationEventData eventData;
    private EventType type = null;

    public NotificationSlot(Context context) {
        super(context);
    }

    @Override
    public void set(@NonNull EventData data) {
        if (data instanceof NotificationEventData) {
            eventData = (NotificationEventData) data;
            type = data.type();
            if (type == EventType.after) {
                setRetriggerable(false);
            } else if (type == EventType.is) {
                setRetriggerable(true);
            } else {
                throw new IllegalAccessError();
            }
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public boolean isValid() {
        if (!eventData.isValid())
            return false;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void listen() {
        NotificationEventListenerService.listen(context, eventData, notifySelfIntent_positive, notifySelfIntent_negative);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void cancel() {
        NotificationEventListenerService.cancel(context, eventData, notifySelfIntent_positive, notifySelfIntent_negative);
    }

    @Override
    public void check() {
    }

    @Override
    public boolean canPromoteSub() {
        if (type == EventType.after)
            return true;
        else if (type == EventType.is)
            return false;
        else
            throw new IllegalAccessError();
    }
}