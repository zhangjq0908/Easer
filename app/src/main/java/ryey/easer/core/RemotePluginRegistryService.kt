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

package ryey.easer.core

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.*
import android.support.v4.util.ArraySet
import android.util.Log
import com.orhanobut.logger.Logger
import ryey.easer.commons.plugindef.operationplugin.Category
import ryey.easer.commons.plugindef.operationplugin.OperationData
import ryey.easer.core.RemotePluginCommunicationHelper.C
import ryey.easer.remote_plugin.RemoteOperationData
import ryey.easer.remote_plugin.RemotePlugin

/**
 * TODO: Support more remote plugin types
 */
class RemotePluginRegistryService : Service() {

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (RemotePlugin.ACTION_RESPONSE_PLUGIN_INFO == intent.action) {
                Logger.d("[RemotePluginRegistryService][onReceive] %s", intent)
                val packageName = intent.getStringExtra(RemotePlugin.EXTRA_PACKAGE_NAME)
                val pluginId = intent.getStringExtra(RemotePlugin.EXTRA_PLUGIN_ID)
                val pluginName = intent.getStringExtra(RemotePlugin.EXTRA_PLUGIN_NAME)
                val pluginType = intent.getStringExtra(RemotePlugin.EXTRA_PLUGIN_TYPE)
                //TODO: More types
                assert(pluginType == RemotePlugin.TYPE_OPERATION_PLUGIN)
                val categoryString = intent.getStringExtra(RemotePlugin.OperationPlugin.EXTRA_PLUGIN_CATEGORY)
                val category = try {
                    Category.valueOf(categoryString)
                } catch (e: RuntimeException) {
                    Category.unknown
                }
                val info = RemoteOperationPluginInfo(packageName, pluginId, pluginName, category)
                operationPluginInfos.add(info)
            }
        }
    }
    private val mFilter = IntentFilter()

    init {
        mFilter.addAction(RemotePlugin.ACTION_RESPONSE_PLUGIN_INFO)
    }

    private val operationPluginInfos = ArraySet<RemoteOperationPluginInfo>()
    fun infoForId(id: String): RemoteOperationPluginInfo? {
        for (info in operationPluginInfos) {
            if (info.pluginId == id)
                return info
        }
        return null
    }

    private val handlerThread = HandlerThread("RemotePluginRegistryService_HandlerThread")
    private lateinit var incomingHandler: Handler
    private lateinit var incomingMessenger: Messenger

    override fun onCreate() {
        handlerThread.start()
        incomingHandler = IncomingHandler(this, handlerThread)
        incomingMessenger = Messenger(incomingHandler)
        registerReceiver(mReceiver, mFilter)
        queryPlugins()
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely()
        } else {
            handlerThread.quit()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return incomingMessenger.binder
    }

    private fun queryPlugins() {
        operationPluginInfos.clear()
        val resolved = this.packageManager.queryBroadcastReceivers(Intent(RemotePlugin.TYPE_OPERATION_PLUGIN), 0)
        Logger.d("queryPlugin size %d", resolved.size)
        for (ri in resolved) {
            Logger.d("%s %s", ri, ri.activityInfo.packageName)
            val intent = Intent(RemotePlugin.ACTION_REQUEST_PLUGIN_INFO)
            intent.setPackage(ri.activityInfo.packageName)
            sendBroadcast(intent)
        }
    }

    /**
     * FIXME: Fix possible memory leak using WeakReference?
     */
    internal class IncomingHandler(val service: RemotePluginRegistryService,
                                   handlerThread: HandlerThread)
        : Handler(handlerThread.looper) {
        override fun handleMessage(message: Message) {
            Logger.d("[RemotePluginRegistryService][handleMessage] %s", message)
            val rMessenger = message.replyTo
            if (message.what == C.MSG_FIND_PLUGIN) {
                val id = message.data.getString(C.EXTRA_PLUGIN_ID)
                val reply = Message.obtain()
                reply.what = C.MSG_FIND_PLUGIN_RESPONSE
                reply.data.putParcelable(C.EXTRA_PLUGIN_INFO, service.infoForId(id))
                rMessenger.send(reply)
            } else if (message.what == C.MSG_CURRENT_OPERATION_PLUGIN_LIST) {
                val reply = Message.obtain()
                reply.what = C.MSG_CURRENT_OPERATION_PLUGIN_LIST_RESPONSE
                reply.data.putParcelableArrayList(C.EXTRA_PLUGIN_LIST, ArrayList<RemoteOperationPluginInfo>(service.operationPluginInfos))
                rMessenger.send(reply)
            } else if (message.what == C.MSG_PARSE_OPERATION_DATA) {
                throw IllegalAccessError("This message is not yet in use")
                Logger.d("[RemotePluginRegistryService] MSG_PARSE_OPERATION_DATA")
                val id = message.data.getString(C.EXTRA_PLUGIN_ID)
                val pluginInfo = service.infoForId(id)
                val reply = Message.obtain()
                reply.what = C.MSG_PARSE_OPERATION_DATA_RESPONSE
                if (pluginInfo == null) {
                    rMessenger.send(reply)
                } else {
                    val data = message.data.getString(C.EXTRA_RAW_DATA)
                    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
                        override fun onReceive(p0: Context?, p1: Intent?) {
                            if (RemotePlugin.ACTION_RESPONSE_PARSE_DATA == p1?.action) {
                                val operationData: OperationData = p1.getParcelableExtra(RemotePlugin.EXTRA_DATA)
                                reply.data.putParcelable(C.EXTRA_PLUGIN_DATA, operationData)
                                rMessenger.send(reply)
                                service.unregisterReceiver(this)
                            }
                        }
                    }
                    val uri: Uri = Uri.parse("re_ser://%d".format(receiver.hashCode()))
                    val filter = IntentFilter()
                    filter.addAction(RemotePlugin.ACTION_RESPONSE_PARSE_DATA)
                    filter.addDataScheme(uri.scheme)
                    filter.addDataAuthority(uri.authority, null)
                    filter.addDataPath(uri.path, PatternMatcher.PATTERN_LITERAL)
                    service.registerReceiver(receiver, filter)
                    val intent = Intent(RemotePlugin.ACTION_REQUEST_PARSE_DATA)
//                    intent.data = uri
                    intent.`package` = pluginInfo.packageName
                    intent.putExtra(RemotePlugin.EXTRA_DATA, data)
                    service.sendBroadcast(intent)
                }
            } else if (message.what == C.MSG_TRIGGER_OPERATION) {
                Log.d("RemoPlRegistry", "MSG_TRIGGER_OPERATION")
                message.data.classLoader = String.javaClass.classLoader
                val id = message.data.getString(C.EXTRA_PLUGIN_ID)
//                message.data.classLoader = RemoteOperationData.javaClass.classLoader
                val data: RemoteOperationData = message.data.getParcelable(C.EXTRA_PLUGIN_DATA)
                val pluginInfo = service.infoForId(id)!!
                val intent = Intent(RemotePlugin.OperationPlugin.ACTION_TRIGGER)
                intent.`package` = pluginInfo.packageName
                intent.putExtra(RemotePlugin.EXTRA_DATA, data)
                service.sendBroadcast(intent)
            } else if (message.what == C.MSG_EDIT_OPERATION_DATA) {
                val id = message.data.getString(C.EXTRA_PLUGIN_ID)
                val pluginInfo = service.infoForId(id)!!
                val bundle = Bundle()
                bundle.putString(C.EXTRA_PLUGIN_PACKAGE, pluginInfo.packageName)
                bundle.putString(C.EXTRA_PLUGIN_EDIT_DATA_ACTIVITY, "%s.EditDataActivity".format(pluginInfo.packageName))
                val reply = Message.obtain()
                reply.what = C.MSG_EDIT_OPERATION_DATA_RESPONSE
                reply.data = bundle
                rMessenger.send(reply)
            }
        }
    }

}
