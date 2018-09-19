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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.ArraySet;

import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ryey.easer.commons.local_plugin.operationplugin.OperationData;
import ryey.easer.core.data.ProfileStructure;
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

    private OnPluginFoundCallback onPluginFoundCallback;
    private OnOperationPluginListObtainedCallback onOperationPluginListObtainedCallback;
    private OnEditDataIntentObtainedCallback onEditDataIntentObtainedCallback;

    public RemotePluginCommunicationHelper(@NonNull Context context) {
        this.context = context;
    }

    public void begin() {
        Logger.d("[RemotePluginCommunicationHelper] begin()");
        Intent intent = new Intent(context, RemotePluginRegistryService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private DelayedTaskUntilConnectedWrapper delayedTaskUntilConnectedWrapper = new DelayedTaskUntilConnectedWrapper();
    public void doAfterConnect(Callable<Void> task) {
        delayedTaskUntilConnectedWrapper.doAfterConnect(task);
    }

    private Lock lck_operationDataParsedCallbackMap = new ReentrantLock();
    private Map<ParcelUuid, OnOperationDataParsedCallback> onOperationDataParsedCallbackMap = new ArrayMap<>();

    //TODO: Generify for every action
    //TODO: Implement semi-parse
    public Callable<Void> onConnectedParseOperationData(final String id, final String rawData, final ProfileStructure profileStructure) {
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final ParcelUuid msg_id = new ParcelUuid(UUID.randomUUID());
                lck_operationDataParsedCallbackMap.lock();
                try {
                    onOperationDataParsedCallbackMap.put(msg_id, new OnOperationDataParsedCallback() {
                        @Override
                        public void onOperationDataParsed(@NonNull OperationData data) {
//                            profileStructure.onRemoteDataParsed(id, data);
                            lck_operationDataParsedCallbackMap.lock();
                            try {
                                onOperationDataParsedCallbackMap.remove(msg_id);
                            } finally {
                                lck_operationDataParsedCallbackMap.unlock();
                            }
                        }
                    });
                } finally {
                    lck_operationDataParsedCallbackMap.unlock();
                }
                Message message = Message.obtain();
                message.what = C.MSG_PARSE_OPERATION_DATA;
                message.replyTo = inMessenger;
                Bundle bundle = new Bundle();
                bundle.putString(C.EXTRA_PLUGIN_ID, id);
                bundle.putString(C.EXTRA_RAW_DATA, rawData);
                bundle.putParcelable(C.EXTRA_MESSAGE_ID, msg_id);
                message.setData(bundle);
                try {
                    outMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    synchronized public void asyncFindPlugin(final String id, OnPluginFoundCallback onPluginFoundCallback) {
        this.onPluginFoundCallback = onPluginFoundCallback;
        doAfterConnect(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Message message = Message.obtain();
                message.what = C.MSG_FIND_PLUGIN;
                message.replyTo = inMessenger;
                Bundle bundle = new Bundle();
                bundle.putString(C.EXTRA_PLUGIN_ID, id);
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
        this.onOperationPluginListObtainedCallback = onOperationPluginListObtainedCallback;
        doAfterConnect(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Message message = Message.obtain();
                message.what = C.MSG_CURRENT_OPERATION_PLUGIN_LIST;
                message.replyTo = inMessenger;
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
        doAfterConnect(onConnectedTriggerOperation(id, data));
    }

    public Callable<Void> onConnectedTriggerOperation(final String id, final RemoteOperationData data) {
        return new Callable<Void>() {
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
        };
    }

    public void asyncRemoteEditOperationData(String id, OnEditDataIntentObtainedCallback onEditDataIntentObtainedCallback) {
        doAfterConnect(onConnectedEditRemoteOperationData(id, onEditDataIntentObtainedCallback));
    }

    public Callable<Void> onConnectedEditRemoteOperationData(final String id, OnEditDataIntentObtainedCallback onEditDataIntentObtainedCallback) {
        this.onEditDataIntentObtainedCallback = onEditDataIntentObtainedCallback;
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Message message = Message.obtain();
                message.what = C.MSG_EDIT_OPERATION_DATA;
                message.replyTo = inMessenger;
                Bundle bundle = new Bundle();
                bundle.putString(C.EXTRA_PLUGIN_ID, id);
                //assumes only one EditData is in progress globally, so no MSG_ID is used
                message.setData(bundle);
                outMessenger.send(message);
                return null;
            }
        };
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
                if (ref.get().onPluginFoundCallback != null) {
                    ref.get().onPluginFoundCallback.onPluginFound(info);
                    ref.get().onPluginFoundCallback = null;
                }
            } else if (msg.what == C.MSG_CURRENT_OPERATION_PLUGIN_LIST_RESPONSE) {
                msg.getData().setClassLoader(RemoteOperationPluginInfo.class.getClassLoader()); // Required (for strange reason); otherwise ClassNotFound
                ArrayList<RemoteOperationPluginInfo> infoList = msg.getData().getParcelableArrayList(C.EXTRA_PLUGIN_LIST);
                Set<RemoteOperationPluginInfo> infoSet = new ArraySet<>(infoList);
                if (ref.get().onOperationPluginListObtainedCallback != null) {
                    ref.get().onOperationPluginListObtainedCallback.onListObtained(infoSet);
                    ref.get().onOperationPluginListObtainedCallback = null;
                }
            } else if (msg.what == C.MSG_PARSE_OPERATION_DATA_RESPONSE) {
                OperationData operationData = msg.getData().getParcelable(C.EXTRA_PLUGIN_DATA);
                ParcelUuid msg_id = msg.getData().getParcelable(C.EXTRA_MESSAGE_ID);
                ref.get().lck_operationDataParsedCallbackMap.lock();
                try {
                    ref.get().onOperationDataParsedCallbackMap.get(msg_id)
                            .onOperationDataParsed(operationData);
                } finally {
                    ref.get().lck_operationDataParsedCallbackMap.unlock();
                }
            } else if (msg.what == C.MSG_EDIT_OPERATION_DATA_RESPONSE) {
                Bundle bundle = msg.getData();
                String packageName = bundle.getString(C.EXTRA_PLUGIN_PACKAGE);
                String activityName = bundle.getString(C.EXTRA_PLUGIN_EDIT_DATA_ACTIVITY);
                assert packageName != null;
                assert activityName != null;
                Intent editDataIntent = new Intent();
                editDataIntent.setComponent(new ComponentName(packageName, activityName));
                ref.get().onEditDataIntentObtainedCallback.onEditDataIntentObtained(editDataIntent);
            }
        }
    }

    public interface OnConnectedCallback {
        void onConnected(Messenger outMessenger);
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

    static class DelayedTaskUntilConnectedWrapper {

        private AtomicBoolean connected = new AtomicBoolean(false);

        Lock lck_tasks = new ReentrantLock();
        List<Callable<Void>> tasksAfterConnect = new ArrayList<>();

        private OnConnectedCallback onConnectedDoTasksCallback = new OnConnectedCallback() {
            @Override
            public void onConnected(Messenger outMessenger) {
                lck_tasks.lock();
                try {
                    for (int i = tasksAfterConnect.size() - 1; i >= 0; i--) {
                        Callable<Void> task = tasksAfterConnect.get(i);
                        try {
                            task.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tasksAfterConnect.remove(i);
                    }
                } finally {
                    lck_tasks.unlock();
                }
            }
        };

        public void onConnected(Messenger outMessenger) {
            connected.set(true);
            onConnectedDoTasksCallback.onConnected(outMessenger);
        }

        public void onDisconnected() {
            connected.set(false);
        }

        public void doAfterConnect(Callable<Void> task) {
            lck_tasks.lock();
            try {
                if (connected.get()) {
                    try {
                        task.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    tasksAfterConnect.add(task);
                }
            } finally {
                lck_tasks.unlock();
            }
        }
    }

}
