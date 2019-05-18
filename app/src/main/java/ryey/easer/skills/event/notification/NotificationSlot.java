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

package ryey.easer.skills.event.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.orhanobut.logger.Logger;

import ryey.easer.skills.event.SelfNotifiableSlot;

public class NotificationSlot extends SelfNotifiableSlot<NotificationEventData> {

    public NotificationSlot(Context context, NotificationEventData data) {
        this(context, data, true, PERSISTENT_DEFAULT);
    }

    NotificationSlot(Context context, NotificationEventData data, boolean retriggerable, boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void listen() {
        NotificationEventListenerService.listen(context, eventData, uri);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void cancel() {
        NotificationEventListenerService.cancel(context, eventData, uri);
    }

    @Override
    protected void onPositiveNotified(Intent intent) {
        Logger.v("onPositiveNotified");
        changeSatisfiedState(true, intent.getExtras());
    }

    @Override
    protected void onNegativeNotified(Intent intent) {
        Logger.v("onNegativeNotified");
        changeSatisfiedState(false, intent.getExtras());
    }

}