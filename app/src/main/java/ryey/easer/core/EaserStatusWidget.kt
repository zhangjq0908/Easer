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

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import ryey.easer.R
import ryey.easer.core.ui.MainActivity

/**
 * Implementation of App Widget functionality.
 */
class EaserStatusWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ACTION_CLICK == intent?.action || EHService.ACTION_STATE_CHANGED == intent?.action) {
            val views = createViews(context)
            AppWidgetManager.getInstance(context)
                    .updateAppWidget(ComponentName(context!!, EaserStatusWidget::class.java), views)
        } else {
            super.onReceive(context, intent)
        }
    }

    companion object {

        const val ACTION_CLICK = "ryey.easer.core.EaserStatusWidget.ACTION_CLICK"

        private fun createViews(context: Context?): RemoteViews {
            val views = RemoteViews(context?.packageName, R.layout.easer_status_widget)
            if (EHService.isRunning()) {
                views.setImageViewResource(R.id.appwidget_status, R.drawable.ic_status_positive)
            } else {
                views.setImageViewResource(R.id.appwidget_status, R.drawable.ic_status_negative)
            }
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            views.setOnClickPendingIntent(R.id.layout, pendingIntent)
            views.setOnClickPendingIntent(R.id.appwidget_status, pendingIntent)
            return views
        }

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {
            val views = createViews(context)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

