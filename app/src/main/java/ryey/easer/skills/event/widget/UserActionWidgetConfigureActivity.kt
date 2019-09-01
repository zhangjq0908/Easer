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

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText

import ryey.easer.R
import ryey.easer.commons.ui.CommonBaseActivity

/**
 * The configuration screen for the [UserActionWidget] AppWidget.
 */
class UserActionWidgetConfigureActivity : CommonBaseActivity() {

    internal var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    internal var mAppWidgetText: EditText? = null
    internal var mAppWidgetTag: EditText? = null
    internal var mOnClickListener: View.OnClickListener = View.OnClickListener {
        val context = this@UserActionWidgetConfigureActivity

        // When the button is clicked, store the string locally
        val widgetText = mAppWidgetText?.text.toString()
        val widgetTag = mAppWidgetTag?.text.toString()
        savePref(context, mAppWidgetId, widgetText, widgetTag)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        UserActionWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(Activity.RESULT_CANCELED)

        setContentView(R.layout.widget_event_widget_configure)
        mAppWidgetText = findViewById<View>(R.id.appwidget_text) as EditText
        mAppWidgetTag = findViewById<View>(R.id.appwidget_tag) as EditText
        findViewById<View>(R.id.add_button).setOnClickListener(mOnClickListener)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        mAppWidgetText?.setText(loadTextPref(this@UserActionWidgetConfigureActivity, mAppWidgetId))
        mAppWidgetTag?.setText(loadTagPref(this@UserActionWidgetConfigureActivity, mAppWidgetId))
    }

    companion object {

        private val PREFS_NAME = "ryey.easer.skills.event.widget.UserActionWidget"
        private val PREF_PREFIX_KEY = "appwidget_"
        private val PREF_TEXT = "_text"
        private val PREF_TAG = "_tag"

        private fun keyOfText(appWidgetId: Int): String {
            return PREF_PREFIX_KEY + appWidgetId + PREF_TEXT
        }

        private fun keyOfTag(appWidgetId: Int): String {
            return PREF_PREFIX_KEY + appWidgetId + PREF_TAG
        }

        internal fun loadTextPref(context: Context, appWidgetId: Int): String {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val titleValue = prefs.getString(keyOfText(appWidgetId), null)
            return titleValue ?: context.getString(R.string.event_widget_default_text)
        }

        internal fun loadTagPref(context: Context, appWidgetId: Int): String {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            return prefs.getString(keyOfTag(appWidgetId), null) ?: context.getString(R.string.event_widget_default_tag)
        }

        internal fun savePref(context: Context, appWidgetId: Int, widgetText: String, widgetTag: String) {
            context.getSharedPreferences(PREFS_NAME, 0).edit()
                    .putString(keyOfText(appWidgetId), widgetText)
                    .putString(keyOfTag(appWidgetId), widgetTag)
                    .apply()
        }

        internal fun deletePref(context: Context, appWidgetId: Int) {
            context.getSharedPreferences(PREFS_NAME, 0).edit()
                    .remove(keyOfText(appWidgetId))
                    .remove(keyOfTag(appWidgetId))
                    .apply()
        }
    }
}

