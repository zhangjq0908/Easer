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

package ryey.easer.plugins.operation.send_notification;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import java.util.concurrent.ThreadLocalRandom;

import ryey.easer.R;
import ryey.easer.commons.plugindef.operationplugin.OperationLoader;

public class SendNotificationLoader extends OperationLoader<SendNotificationOperationData> {
    private static int NOTIFICATION_ID = 0;

    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NOTIFICATION_ID = ThreadLocalRandom.current().nextInt();
        }
    }

    public SendNotificationLoader(Context context) {
        super(context);
    }

    @Override
    public boolean _load(@NonNull SendNotificationOperationData data) {
        NotificationContent content = data.notificationContent;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(content.title);
        builder.setContentText(content.content);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
        NOTIFICATION_ID++;

        return true;
    }
}
