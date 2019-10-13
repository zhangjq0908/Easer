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

package ryey.easer.core.ui.version_n_info

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import ryey.easer.R
import ryey.easer.core.data.storage.StorageHelper

object Version {

    private fun showPrompt(context: Context, @StringRes title: Int, @StringRes body: Int, @StringRes vararg fmt: Int) {
        val db = AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(R.string.button_understand) { dialogInterface, i -> dialogInterface.dismiss() }
        if (fmt.isEmpty()) {
            db.setMessage(body)
        } else {
            db.setMessage(context.getString(body).format(*fmt.map { res -> context.getString(res) }.toTypedArray()))
        }
        (db.show().findViewById<View>(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showVersionPrompt(context: Context, positiveAction: (context: Context) -> Unit, @StringRes body: Int, @StringRes vararg fmt: Int) {
        val db = AlertDialog.Builder(context)
                .setTitle(R.string.message_future_change_title)
                .setPositiveButton(R.string.button_understand) { dialogInterface, _ ->
                    run {
                        positiveAction(context)
                        dialogInterface.dismiss()
                    }
                }
                .setNegativeButton(R.string.button_read_next_time) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
        if (fmt.isEmpty()) {
            db.setMessage(body)
        } else {
            db.setMessage(context.getString(body).format(*fmt.map { res -> context.getString(res) }.toTypedArray()))
        }
        (db.show().findViewById<View>(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
    }

    fun dataVersionChange(context: Context) {
        if (StorageHelper.hasOldData(context)) {
            showPrompt(context, R.string.alert_update_storage_data_title, R.string.alert_update_storage_data)
        }
    }

    fun nearFutureChange(context: Context) {
        if (shouldShowVersionPrompt(context, PREF_DEPRECATE_TREE_VIEW)) { //v0.8
            showVersionPrompt(context, {c -> setVersionPromptKnown(c, PREF_DEPRECATE_TREE_VIEW)}, R.string.message_future_change_0_8)
        }
    }

    private fun shouldShowVersionPrompt(context: Context, name: String): Boolean {
        val prefVersion: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return !prefVersion.getBoolean(name, false)
    }

    private fun setVersionPromptKnown(context: Context, name: String) {
        val prefVersion: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefVersion.edit().putBoolean(name, true).apply()
    }

    const val PREFS_NAME = "version_change"
    const val PREF_DEPRECATE_TREE_VIEW = "deprecate_tree_view"
}