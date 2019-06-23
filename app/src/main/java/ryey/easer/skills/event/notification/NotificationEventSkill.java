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

package ryey.easer.skills.event.notification;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.eventskill.EventDataFactory;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.event.AbstractSlot;

public class NotificationEventSkill implements EventSkill<NotificationEventData> {

    @NonNull
    @Override
    public String id() {
        return "notification";
    }

    @Override
    public int name() {
        return R.string.event_notification;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        //TODO support Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return SkillUtils.isPermissionGrantedForNotificationListenerService(context, NotificationEventListenerService.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        if (!SkillUtils.isPermissionGrantedForNotificationListenerService(activity, NotificationEventListenerService.class)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            } else {
                SkillUtils.requestPermission(activity, requestCode,
                        Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE);
            }
        }
        SkillUtils.reenableComponent(activity, NotificationEventListenerService.class);
    }

    @NonNull
    @Override
    public EventDataFactory<NotificationEventData> dataFactory() {
        return new NotificationEventDataFactory();

    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.android;
    }

    @NonNull
    @Override
    public SkillView<NotificationEventData> view() {
        return new NotificationSkillViewFragment();
    }

    @Override
    public AbstractSlot<NotificationEventData> slot(@NonNull Context context, @ValidData @NonNull NotificationEventData data) {
        return new NotificationSlot(context, data);
    }

    @Override
    public AbstractSlot<NotificationEventData> slot(@NonNull Context context, @NonNull NotificationEventData data, boolean retriggerable, boolean persistent) {
        return new NotificationSlot(context, data, retriggerable, persistent);
    }

}
