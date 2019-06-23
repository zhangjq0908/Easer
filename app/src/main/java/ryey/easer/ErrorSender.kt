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

package ryey.easer

import android.content.Context
import org.acra.ReportField
import org.acra.data.CrashReportData
import org.acra.sender.ReportSender
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class ErrorSender : ReportSender {
    override fun send(context: Context, errorContent: CrashReportData) {
        if (SettingsUtils.logging(context)) {
            val dir = File(EaserApplication.LOG_DIR)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val dateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
            val date = Date()
            val filename = dateFormat.format(date) + ".log"
            val reportFile = File(dir, filename)
            FileWriter(reportFile).use {
                for (elem in FIELDS) {
                    it.append("%s: %s\n".format(elem, errorContent.getString(elem)))
                }
            }

        }
    }

    companion object {
        val FIELDS = arrayOf(
                ReportField.BUILD_CONFIG,
                ReportField.APP_VERSION_CODE,
                ReportField.USER_CRASH_DATE,
                ReportField.ANDROID_VERSION,
                ReportField.BRAND,
                ReportField.PHONE_MODEL,
                ReportField.PRODUCT,
                ReportField.STACK_TRACE
        )
    }
}