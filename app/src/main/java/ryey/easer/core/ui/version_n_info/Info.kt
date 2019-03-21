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
import android.preference.PreferenceManager
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import ryey.easer.R

object Info {
    fun welcome(context: Context) {
        // Show Welcome page at first launch
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_pref_welcome), true)) {
            (AlertDialog.Builder(context)
                    .setTitle(R.string.title_welcome_message)
                    .setMessage(R.string.welcome_message)
                    .setPositiveButton(R.string.button_understand) { dialog, id ->
                        PreferenceManager.getDefaultSharedPreferences(context)
                                .edit()
                                .putBoolean(context.getString(R.string.key_pref_welcome), false)
                                .apply()
                    }
                    .setNegativeButton(R.string.button_read_next_time, null)
                    .show()
                    .findViewById<View>(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
        }
    }
}