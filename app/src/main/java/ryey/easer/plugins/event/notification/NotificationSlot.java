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

package ryey.easer.plugins.event.notification;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public class NotificationSlot extends AbstractSlot<NotificationEventData> {
    private EventType type = null;

    public NotificationSlot(Context context, NotificationEventData data) {
        this(context, data, isRetriggerable(data), PERSISTENT_DEFAULT);
    }

    NotificationSlot(Context context, NotificationEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
        type = data.type();
    }

    private static boolean isRetriggerable(NotificationEventData data) {
        EventType type = data.type();
        if (type == EventType.after) {
            return false;
        } else if (type == EventType.is) {
            return true;
        } else {
            throw new IllegalAccessError();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void listen() {
        NotificationEventListenerService.listen(context, eventData, notifyLotusIntent, notifyLotusUnsatisfiedIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void cancel() {
        NotificationEventListenerService.cancel(context, eventData, notifyLotusIntent, notifyLotusUnsatisfiedIntent);
    }

    @Override
    public void check() {
    }

}