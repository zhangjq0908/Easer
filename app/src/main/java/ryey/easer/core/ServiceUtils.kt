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

package ryey.easer.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ryey.easer.R
import ryey.easer.SettingsUtils
import ryey.easer.core.ui.MainActivity

class ServiceUtils {

    companion object {

        val EXTRA_RUN_WITH_UI = "ryey.easer.core.ServiceUtils.EXTRA.RUN_WITH_UI"
        
        private val NOTIFICATION_ID = 1

        private var startCount = 0
        private var stopCount = 0
        
        fun startNotification(service: Service) {
            startCount++
            if (!SettingsUtils.showNotification(service))
                return
            val notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            val builder: NotificationCompat.Builder

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "easer_ind"
                val channelName = "Easer Service Indicator"
                val importance = NotificationManager.IMPORTANCE_LOW
                val notificationChannel = NotificationChannel(channelId, channelName, importance)
                notificationManager!!.createNotificationChannel(notificationChannel)
                builder = NotificationCompat.Builder(service, channelId)
                builder.setAutoCancel(true)
            } else {
                @Suppress("DEPRECATION")
                builder = NotificationCompat.Builder(service)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
            }
            val REQ_CODE = 0
            val pendingIntent = PendingIntent.getActivity(
                    service, REQ_CODE, Intent(service, MainActivity::class.java), 0)
            builder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(service.getString(
                            R.string.text_notification_running_indicator_content,
                            service.getString(R.string.easer)))
                    .setOngoing(true)
                    .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                    .setContentIntent(pendingIntent)

            val indicatorNotification = builder.build()

            if (SettingsUtils.runInForeground(service)) {
                service.startForeground(NOTIFICATION_ID, indicatorNotification)
            } else {
                notificationManager!!.notify(NOTIFICATION_ID, indicatorNotification)
            }
        }

        fun stopNotification(service: Service) {
            stopCount++
            if (!SettingsUtils.showNotification(service))
                return
            if (SettingsUtils.runInForeground(service)) {

            } else {
                val notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
                notificationManager!!.cancel(NOTIFICATION_ID)
            }
        }
    }

}