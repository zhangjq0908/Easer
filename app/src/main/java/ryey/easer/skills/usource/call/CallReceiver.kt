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

package ryey.easer.skills.usource.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.TelephonyManager
import ryey.easer.Utils

internal class CallReceiver(private val callEventHandler: CallEventHandler) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val state: String = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val number: String = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        when (state) {
            TelephonyManager.EXTRA_STATE_IDLE -> callEventHandler.onIdle(number)
            TelephonyManager.EXTRA_STATE_RINGING -> callEventHandler.onRinging(number)
            TelephonyManager.EXTRA_STATE_OFFHOOK -> callEventHandler.onOffHook(number)
        }
    }

    companion object {
        val callFilter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)

        private fun handleState(data: CallUSourceData, number: String, state: CallUSourceData.CallState): Boolean {
            return if (data.callStates.contains(state)) {
                if (Utils.isBlank(data.number)) {
                    true
                } else {
                    if (data.number != null) {
                        number.contains(data.number)
                    } else {
                        false
                    }
                }
            } else {
                false
            }
        }

        fun handleIdle(data: CallUSourceData, number: String): Boolean {
            return handleState(data, number, CallUSourceData.CallState.IDLE)
        }

        fun handleRinging(data: CallUSourceData, number: String): Boolean {
            return handleState(data, number, CallUSourceData.CallState.RINGING)
        }

        fun handleOffHook(data: CallUSourceData, number: String): Boolean {
            return handleState(data, number, CallUSourceData.CallState.OFFHOOK)
        }
    }

    interface CallEventHandler {
        fun onIdle(number: String)
        fun onRinging(number: String)
        fun onOffHook(number: String)
    }
}
