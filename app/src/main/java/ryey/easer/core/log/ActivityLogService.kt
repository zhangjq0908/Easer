/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.core.log

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import com.orhanobut.logger.Logger
import ryey.easer.core.EHService

import java.util.*

class ActivityLogService : Service() {

    internal val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ACTION_NEW_LOG_ENTRY == action) {
                if (intent.extras == null) {
                    Logger.wtf("ACTION_NEW_LOG_ENTRY Intent has null extras???")
                    return
                }
                ActivityLogService.recordProfile(intent.extras!!)
                val intent1 = Intent()
                intent1.action = EHService.ACTION_PROFILE_UPDATED
                sendBroadcast(intent1)
            }
        }
    }

    internal val mFilter: IntentFilter = IntentFilter()

    init {
        mFilter.addAction(ACTION_NEW_LOG_ENTRY)
    }

    override fun onCreate() {
        registerReceiver(mReceiver, mFilter)
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
    }

    override fun onBind(intent: Intent): IBinder? {
        return ActivityLogServiceBinder(this)
    }

    class ActivityLogServiceBinder(val service: ActivityLogService): Binder() {
        fun clearLog() {
            ActivityLogService.activityLogList.clear()
        }
    }

    companion object {
        private const val ACTION_NEW_LOG_ENTRY = "ryey.easer.action.PROFILE_LOADED"
        private const val EXTRA_ACTIVITY_LOG = "ryey.easer.core.log.EXTRA.ACTIVITY_LOG"

        private val activityLogList = LinkedList<ActivityLog>()

        private fun notifyLog(context: Context, log: ActivityLog) {
            val intent = Intent(ACTION_NEW_LOG_ENTRY)
            intent.putExtra(EXTRA_ACTIVITY_LOG, log)
            context.sendBroadcast(intent)
        }

        fun notifyServiceStatus(context: Context, serviceName: String, start: Boolean, extraInfo: String?) {
            val log = ServiceLog(serviceName, start, extraInfo)
            notifyLog(context, log)
        }

        fun notifyScriptSatisfied(context: Context, scriptName: String, profileName: String? = null, extraInfo: String? = null) {
            val log = ScriptSatisfactionLog(scriptName, true, profileName, extraInfo)
            notifyLog(context, log)
        }

        fun notifyScriptUnsatisfied(context: Context, scriptName: String, extraInfo: String? = null) {
            val log = ScriptSatisfactionLog(scriptName, false, null, extraInfo)
            notifyLog(context, log)
        }

        fun notifyProfileLoaded(context: Context, profileName: String, extraInfo: String?) {
            val log = ProfileLoadedLog(profileName, extraInfo)
            val intent = Intent(ACTION_NEW_LOG_ENTRY)
            intent.putExtra(EXTRA_ACTIVITY_LOG, log)
            context.sendBroadcast(intent)
        }

        @Synchronized
        fun recordProfile(bundle: Bundle) {
            val activityLog: ActivityLog = bundle.getParcelable(EXTRA_ACTIVITY_LOG)
            activityLogList.addLast(activityLog)
        }

        val lastHistory: ActivityLog?
            get() = if (activityLogList.size == 0) null else activityLogList.last

        val history: List<ActivityLog>
            get() = activityLogList
    }
}
