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

package ryey.easer.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelUuid;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;

import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.remote_plugin.RemoteOperationData;

public class RemotePluginCommunicationHelper {

    public static class C {

        public static final int MSG_FIND_PLUGIN = 0;
        public static final int MSG_FIND_PLUGIN_RESPONSE = 0;
        public static final String EXTRA_PLUGIN_ID = "ryey.easer.IPC.EXTRA.PLUGIN_ID";
        public static final String EXTRA_PLUGIN_INFO = "ryey.easer.IPC.EXTRA.PLUGIN_INFO";

        public static final int MSG_CURRENT_OPERATION_PLUGIN_LIST = 1;
        public static final int MSG_CURRENT_OPERATION_PLUGIN_LIST_RESPONSE = 1;
        public static final String EXTRA_PLUGIN_LIST = "ryey.easer.IPC.EXTRA.PLUGIN_LIST";

        public static final int MSG_PARSE_OPERATION_DATA = 2;
        public static final int MSG_PARSE_OPERATION_DATA_RESPONSE = 2;
        public static final String EXTRA_MESSAGE_ID = "ryey.easer.IPC.EXTRA.MESSAGE_ID";
        public static final String EXTRA_RAW_DATA = "ryey.easer.IPC.EXTRA.RAW_DATA";
        public static final String EXTRA_PLUGIN_DATA = "ryey.easer.IPC.EXTRA.PLUGIN_DATA";

        public static final int MSG_TRIGGER_OPERATION = 3;

        public static final int MSG_EDIT_OPERATION_DATA = 4;
        public static final int MSG_EDIT_OPERATION_DATA_RESPONSE = 4;
        public static final String EXTRA_PLUGIN_PACKAGE = "ryey.easer.IPC.EXTRA.PLUGIN_PACKAGE";
        public static final String EXTRA_PLUGIN_EDIT_DATA_ACTIVITY = "ryey.easer.IPC.EXTRA.PLUGIN_EDIT_DATA_ACTIVITY";
    }

    private Context context;

    private Messenger outMessenger;
    private IncomingHandler handler = new IncomingHandler(new WeakReference<>(this));
    public final Messenger inMessenger = new Messenger(handler);
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Logger.d("[RemotePluginCommunicationHelper:%s] onServiceConnected", context);
            outMessenger = new Messenger(iBinder);
            delayedTaskUntilConnectedWrapper.onConnected(outMessenger);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            delayedTaskUntilConnectedWrapper.onDisconnected();
            outMessenger = null;
        }
    };

    private DelayedTaskUntilConnectedWrapper delayedTaskUntilConnectedWrapper = new DelayedTaskUntilConnectedWrapper();
    private void doAfterConnect(Callable<Void> task) {
        delayedTaskUntilConnectedWrapper.doAfterConnected(task);
    }

    private final DelayedCallback<OnPluginFoundCallback> onPluginFoundCallbackDelayedCallback = new DelayedCallback<>(new ArrayMap<>());
    private final DelayedCallback<OnOperationPluginListObtainedCallback> onOperationPluginListObtainedCallbackDelayedCallback = new DelayedCallback<>(new ArrayMap<>());
    private final DelayedCallback<OnEditDataIntentObtainedCallback> onEditDataIntentObtainedCallbackDelayedCallback = new DelayedCallback<>(new ArrayMap<>());
    private final DelayedCallback<OnOperationDataParsedCallback> onOperationDataParsedCallbackDelayedCallback = new DelayedCallback<>(new ArrayMap<>());

    public RemotePluginCommunicationHelper(@NonNull Context context) {
        this.context = context;
    }

    public void begin() {
        Logger.d("[RemotePluginCommunicationHelper] begin()");
        Intent intent = new Intent(context, RemotePluginRegistryService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void onConnectedParseOperationData(final String id, final String rawData, final OnOperationDataParsedCallback onOperationDataParsedCallback) {
        ParcelUuid uuid = onOperationDataParsedCallbackDelayedCallback.putCallback(onOperationDataParsedCallback);
        doAfterConnect(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Message message = Message.obtain();
                message.what = C.MSG_PARSE_OPERATION_DATA;
                message.replyTo = inMessenger;
                Bundle bundle = new Bundle();
                bundle.putString(C.EXTRA_PLUGIN_ID, id);
                bundle.putString(C.EXTRA_RAW_DATA, rawData);
                bundle.putParcelable(C.EXTRA_MESSAGE_ID, uuid);
                message.setData(bundle);
                try {
                    outMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    synchronized public void asyncFindPlugin(final String id, OnPluginFoundCallback onPluginFoundCallback) {
        ParcelUuid uuid = onPluginFoundCallbackDelayedCallback.putCallback(onPluginFoundCallback);
        doAfterConnect(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Message message = Message.obtain();
                message.what = C.MSG_FIND_PLUGIN;
                message.replyTo = inMessenger;
                Bundle bundle = new Bundle();
                bundle.putString(C.EXTRA_PLUGIN_ID, id);
                bundle.putParcelable(C.EXTRA_MESSAGE_ID, uuid);
                message.setData(bundle);
                try {
                    outMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    synchronized public void asyncCurrentOperationPluginList(@NonNull OnOperationPluginListObtainedCallback onOperationPluginListObtainedCallback) {
        ParcelUuid uuid = onOperationPluginListObtainedCallbackDelayedCallback.putCallback(onOperationPluginListObtainedCallback);
        doAfterConnect(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Message message = Message.obtain();
                message.what = C.MSG_CURRENT_OPERATION_PLUGIN_LIST;
                message.replyTo = inMessenger;
                Bundle bundle = new Bundle();
                bundle.putParcelable(C.EXTRA_MESSAGE_ID, uuid);
                message.setData(bundle);
                try {
                    outMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public void asyncTriggerOperation(final String id, final RemoteOperationData data) {
        doAfterConnect(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Message message = Message.obtain();
                message.what = C.MSG_TRIGGER_OPERATION;
                message.getData().putString(C.EXTRA_PLUGIN_ID, id);
                message.getData().putParcelable(C.EXTRA_PLUGIN_DATA, data);
                try {
                    outMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public void asyncRemoteEditOperationData(final String id, OnEditDataIntentObtainedCallback onEditDataIntentObtainedCallback) {
        ParcelUuid uuid = onEditDataIntentObtainedCallbackDelayedCallback.putCallback(onEditDataIntentObtainedCallback);
        doAfterConnect(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Message message = Message.obtain();
                message.what = C.MSG_EDIT_OPERATION_DATA;
                message.replyTo = inMessenger;
                Bundle bundle = new Bundle();
                bundle.putString(C.EXTRA_PLUGIN_ID, id);
                bundle.putParcelable(C.EXTRA_MESSAGE_ID, uuid);
                message.setData(bundle);
                outMessenger.send(message);
                return null;
            }
        });
    }

    public void end() {
        context.unbindService(serviceConnection);
    }

    static class IncomingHandler extends Handler {
        WeakReference<RemotePluginCommunicationHelper> ref;

        IncomingHandler(WeakReference<RemotePluginCommunicationHelper> reference) {
            ref = reference;
        }

        @Override
        public void handleMessage(Message msg) {
            Logger.d("[RemotePluginCommunicationHelper][handleMessage] %s", msg);
            if (msg.what == C.MSG_FIND_PLUGIN_RESPONSE) {
                msg.getData().setClassLoader(RemotePluginInfo.class.getClassLoader());
                RemotePluginInfo info = msg.getData().getParcelable(C.EXTRA_PLUGIN_INFO);
                ParcelUuid uuid = msg.getData().getParcelable(C.EXTRA_MESSAGE_ID);
                assert uuid != null;
                OnPluginFoundCallback callback = ref.get().onPluginFoundCallbackDelayedCallback.retrieveCallBack(uuid);
                if (callback != null)
                    callback.onPluginFound(info);
            } else if (msg.what == C.MSG_CURRENT_OPERATION_PLUGIN_LIST_RESPONSE) {
                msg.getData().setClassLoader(RemoteOperationPluginInfo.class.getClassLoader()); // Required (for strange reason); otherwise ClassNotFound
                ArrayList<RemoteOperationPluginInfo> infoList = msg.getData().getParcelableArrayList(C.EXTRA_PLUGIN_LIST);
                Set<RemoteOperationPluginInfo> infoSet = new ArraySet<>(infoList);
                ParcelUuid uuid = msg.getData().getParcelable(C.EXTRA_MESSAGE_ID);
                assert uuid != null;
                OnOperationPluginListObtainedCallback callback = ref.get().onOperationPluginListObtainedCallbackDelayedCallback.retrieveCallBack(uuid);
                if (callback != null)
                    callback.onListObtained(infoSet);
            } else if (msg.what == C.MSG_PARSE_OPERATION_DATA_RESPONSE) {
                OperationData operationData = msg.getData().getParcelable(C.EXTRA_PLUGIN_DATA);
                assert operationData != null;
                ParcelUuid uuid = msg.getData().getParcelable(C.EXTRA_MESSAGE_ID);
                OnOperationDataParsedCallback callback = ref.get().onOperationDataParsedCallbackDelayedCallback.retrieveCallBack(uuid);
                if (callback != null)
                    callback.onOperationDataParsed(operationData);
            } else if (msg.what == C.MSG_EDIT_OPERATION_DATA_RESPONSE) {
                Bundle bundle = msg.getData();
                String packageName = bundle.getString(C.EXTRA_PLUGIN_PACKAGE);
                String activityName = bundle.getString(C.EXTRA_PLUGIN_EDIT_DATA_ACTIVITY);
                assert packageName != null;
                assert activityName != null;
                Intent editDataIntent = new Intent();
                editDataIntent.setComponent(new ComponentName(packageName, activityName));
                ParcelUuid uuid = bundle.getParcelable(C.EXTRA_MESSAGE_ID);
                OnEditDataIntentObtainedCallback callback = ref.get().onEditDataIntentObtainedCallbackDelayedCallback.retrieveCallBack(uuid);
                if (callback != null)
                    callback.onEditDataIntentObtained(editDataIntent);
            }
        }
    }

    public interface OnOperationPluginListObtainedCallback {
        void onListObtained(Set<RemoteOperationPluginInfo> operationPluginInfos);
    }

    public interface OnPluginFoundCallback {
        void onPluginFound(@Nullable RemotePluginInfo info);
    }

    public interface OnOperationDataParsedCallback {
        void onOperationDataParsed(@NonNull OperationData data);
    }

    public interface OnEditDataIntentObtainedCallback {
        void onEditDataIntentObtained(@NonNull Intent editDataIntent);
    }

    static class DelayedTaskUntilConnectedWrapper extends AsyncHelper.DelayedWhenSatisfied {
        void onConnected(Messenger outMessenger) {
            super.onSatisfied();
        }

        void onDisconnected() {
            super.onUnsatisfied();
        }

        void doAfterConnected(Callable<Void> task) {
            super.doAfter(task);
        }
    }

    static class DelayedCallback<T>  {
        private Lock lckCallbackMap = new ReentrantLock();
        private final Map<ParcelUuid, T> callbackMap;

        DelayedCallback(Map<ParcelUuid, T> callbackMap) {
            this.callbackMap = callbackMap;
        }

        ParcelUuid putCallback(T callback) {
            lckCallbackMap.lock();
            try {
                ParcelUuid uuid = new ParcelUuid(UUID.randomUUID());
                callbackMap.put(uuid, callback);
                return uuid;
            } finally {
                lckCallbackMap.unlock();
            }
        }

        T retrieveCallBack(ParcelUuid uuid) {
            lckCallbackMap.lock();
            try {
                T callback = callbackMap.get(uuid);
                if (callback != null)
                    callbackMap.remove(uuid);
                return callback;
            } finally {
                lckCallbackMap.unlock();
            }
        }
    }

}
