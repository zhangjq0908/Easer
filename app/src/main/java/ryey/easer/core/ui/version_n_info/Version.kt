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
import android.support.annotation.StringRes
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
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

    fun dataVersionChange(context: Context) {
        if (StorageHelper.hasOldData(context)) {
            showPrompt(context, R.string.alert_update_storage_data_title, R.string.alert_update_storage_data)
        }
    }

    fun nearFutureChange(context: Context) {
        nearFutureChange_0_7_4(context)
    }

    private fun nearFutureChange_0_7_4(context: Context) {
//        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_pref_passive_mode), false)) {
            showPrompt(context, R.string.message_future_change_title__0_7_4, R.string.message_future_change_body__0_7_4, R.string.pref_passive_mode_title, R.string.title_setting)
//        }
    }
}