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

package ryey.easer.skills.event.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import ryey.easer.R

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [UserActionWidgetConfigureActivity]
 */
class UserActionWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            UserActionWidgetConfigureActivity.deletePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        //TODO: change the (dedicated?) service's lifecycle related to this
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        val ACTION_WIDGET_CLICKED = "ryey.easer.skills.widget.ACTION.WIDGET_CLICKED"
        val EXTRA_WIDGET_TAG = "ryey.easer.skills.widget.EXTRA.WIDGET_TAG"

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            val widgetText = UserActionWidgetConfigureActivity.loadTextPref(context, appWidgetId)
            val widgetTag = UserActionWidgetConfigureActivity.loadTagPref(context, appWidgetId)
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.widget_event_widget)
            views.setTextViewText(R.id.appwidget_text, widgetText)

            val intent = Intent(ACTION_WIDGET_CLICKED)
            intent.putExtra(EXTRA_WIDGET_TAG, widgetTag)
            val pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            views.setOnClickPendingIntent(R.id.layout, pendingIntent)
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

