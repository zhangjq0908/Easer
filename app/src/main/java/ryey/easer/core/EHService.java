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

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import ryey.easer.core.data.LogicGraph;
import ryey.easer.core.data.storage.ScriptDataStorage;
import ryey.easer.core.log.ActivityLogService;

/**
 * The main background service.
 * It maintains several {@link Lotus}(es) and send Intent to load Profile(s) & etc.
 */
public class EHService extends Service implements CoreServiceComponents.LogicManager {
    public static final String ACTION_RELOAD = "ryey.easer.action.RELOAD";

    public static final String ACTION_STATE_CHANGED = "ryey.easer.action.STATE_CHANGED";
    public static final String ACTION_PROFILE_UPDATED = "ryey.easer.action.PROFILE_UPDATED";

    private static final String TAG = "[EHService] ";
    private static final String SERVICE_NAME = "Easer";

    /**
     * A piece of information in Intent to describe the current working context.
     * This is designed for testing use only (at least yet), when binding service.
     * If it's {@link #TEST}, then it returns {@link EHTestBinder} to allow more control from binder for testing.
     * Anything other than {@link #TEST} means normal working context (not testing), and returns {@link EHBinder}.
     */
    static final String ARG_WORKING_CONTEXT = "working_context";
    static final int TEST = 1;

    /**
     * NEW
     * key: Script (node) name
     * value: lotus
     */
    Map<String, LogicManagedLotus> lotusMap = new HashMap<>(); //TODO: merge into LogicGraph ?
    LogicGraph logicGraph;

    /**
     * Necessary objects for the correct functioning of EHService.
     * {@link #jobCH} is for calling functions from {@link ConditionHolderService}
     * {@link #jobLP} is for calling functions from {@link ProfileLoaderService}
     */
    private final CoreServiceComponents.DelayedConditionHolderBinderJobs jobCH = new CoreServiceComponents.DelayedConditionHolderBinderJobs();
    private final AsyncHelper.DelayedLoadProfileJobs jobLP = new AsyncHelper.DelayedLoadProfileJobs();
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Logger.v("%s onServiceConnected: %s", TAG, componentName);
            if (componentName.getClassName().equals(ConditionHolderService.class.getName())) {
                jobCH.onBind((ConditionHolderService.CHBinder) iBinder);
                jobCH.doAfter(() -> {
                    mSetTriggers();
                    return null;
                });
            } else if (componentName.getClassName().equals(ProfileLoaderService.class.getName())) {
                jobLP.onBind((ProfileLoaderService.PLSBinder) iBinder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Logger.v("%s onServiceDisconnected: %s", TAG, componentName);
            if (componentName.getClassName().equals(ConditionHolderService.class.getName())) {
                jobCH.onUnbind();
            } else if (componentName.getClassName().equals(ProfileLoaderService.class.getName())) {
                jobLP.onUnbind();
            }
        }
    };

    private final CoreServiceComponents.CoreSkillHelper coreSkillHelper = new CoreServiceComponents.CoreSkillHelper(this, jobCH);
    public static void registerConditionEventNotifier(@NonNull Context context, @NonNull String conditionName, @NonNull Uri notifyData) {
        CoreServiceComponents.CoreSkillHelper.registerConditionEventNotifier(context, conditionName, notifyData);
    }
    public static void unregisterConditionEventNotifier(@NonNull Context context, @NonNull String conditionName, @NonNull Uri notifyData) {
        CoreServiceComponents.CoreSkillHelper.unregisterConditionEventNotifier(context, conditionName, notifyData);
    }

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.d("Broadcast received :: action: <%s>", action);
            if (ACTION_RELOAD.equals(action)) {
                reloadTriggers();
            }
        }
    };

    private static boolean running = false;

    public static boolean isRunning() {
        return running;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EHService.class);
        ContextCompat.startForegroundService(context, intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, EHService.class);
        context.stopService(intent);
    }

    public static void reload(Context context) {
        Intent intent = new Intent();
        intent.setAction(EHService.ACTION_RELOAD);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        Logger.v(TAG + "onCreate()");
        super.onCreate();
        ServiceUtils.Companion.startNotification(this);
        ActivityLogService.Companion.notifyServiceStatus(this, SERVICE_NAME, true, null);
        bindService(new Intent(this, ConditionHolderService.class), connection, Context.BIND_AUTO_CREATE);
        bindService(new Intent(this, ProfileLoaderService.class), connection, Context.BIND_AUTO_CREATE);
        running = true;
        Intent intent = new Intent(ACTION_STATE_CHANGED);
        sendBroadcast(intent);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RELOAD);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
        coreSkillHelper.onCreate();
        Logger.i(TAG + "created");
    }

    @Override
    public void onDestroy() {
        Logger.v(TAG + "onDestroy");
        super.onDestroy();
        ServiceUtils.Companion.stopNotification(this);
        ActivityLogService.Companion.notifyServiceStatus(this, SERVICE_NAME, false, null);
        mCancelTriggers();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        coreSkillHelper.onDestroy();
        unbindService(connection);
        running = false;
        Intent intent = new Intent(ACTION_STATE_CHANGED);
        sendBroadcast(intent);
        Logger.i(TAG + "destroyed");
    }

    private void reloadTriggers() {
        Logger.v(TAG + "reloadTriggers(): setting job");
        jobCH.doAfter(binder -> {
            Logger.v(TAG + "triggers reloading");
            binder.reload();
            for (LogicGraph.LogicNode node : logicGraph.initialNodes()) {
                LogicManagedLotus logicManagedLotus = lotusMap.get(node.id);
                if (logicManagedLotus != null && logicManagedLotus.getCount() > 0) {
                    mCancelTriggers();
                    break;
                }
            }
            mSetTriggers();
            Logger.d(TAG + "triggers reloaded");
        });
    }

    private void mCancelTriggers() {
        jobCH.doAfter(binder -> {
            for (LogicGraph.LogicNode node : logicGraph.initialNodes()) {
                LogicManagedLotus logicManagedLotus = lotusMap.get(node.id);
                if (logicManagedLotus != null && logicManagedLotus.getCount() > 0) {
                    logicManagedLotus.deactivateFrom("mCancelTriggers");
                }
            }
            lotusMap.clear();

            binder.clearAssociation();
        });
    }

    private void mSetTriggers() {
        final String TAG = "[EHService:mSetTriggers] ";
        Logger.v(TAG + "setting triggers");
        ScriptDataStorage storage = new ScriptDataStorage(this);
        logicGraph = storage.getLogicGraph();
        for (LogicGraph.LogicNode node : logicGraph.initialNodes()) {
            Logger.v(TAG + "setting trigger for <%s>", node.id);
            if (node.active) {
                activateNode(node, null);
                Logger.v(TAG + "trigger for script node <%s> is set", node.id);
            }
        }
        Logger.d(TAG + "triggers have been set");
    }

    @Override
    public IBinder onBind(Intent intent) {
        int working_context = intent.getIntExtra(ARG_WORKING_CONTEXT, 0);
        if (working_context == TEST) {
            return new EHTestBinder();
        } else {
            return new EHBinder();
        }
    }

    /**
     * {@inheritDoc}
     * The current implementation tries to guarantee not to activate the node multiple times.
     * @param node The node to activate.
     * @param from The ID (of a node) from which to activate the specified node.
     */
    @Override
    public void activateNode(LogicGraph.LogicNode node, @Nullable String from) {
        LogicManagedLotus logicManagedLotus = lotusMap.get(node.id);
        if (logicManagedLotus == null) {
            logicManagedLotus = new LogicManagedLotus(Lotus.createLotus(this, node, this, jobCH, jobLP));
            lotusMap.put(node.id, logicManagedLotus);
        }
        logicManagedLotus.activateFrom(from);
    }

    /**
     * {@inheritDoc}
     * The current implementation tries to guarantee not to deactivate the node if irrelevant.
     * @param node The node to deactivate.
     * @param from The ID (of a node) from which to deactivate the specified node.
     */
    @Override
    public void deactivateNode(LogicGraph.LogicNode node, String from) {
        LogicManagedLotus logicManagedLotus = lotusMap.get(node.id);
        if (logicManagedLotus != null)
            logicManagedLotus.deactivateFrom(from);
    }

    @Override
    public void activateSuccessors(LogicGraph.LogicNode node) {
        Logger.d("LML activating from %s", node.id);
        Set<LogicGraph.LogicNode> successors = logicGraph.successors(node);
        if (successors != null) {
            for (LogicGraph.LogicNode successor : successors) {
                if (successor.active)
                    activateNode(successor, node.id);
            }
        }
    }

    @Override
    public void deactivateSuccessors(LogicGraph.LogicNode node) {
        Set<LogicGraph.LogicNode> successors = logicGraph.successors(node);
        if (successors != null) {
            for (LogicGraph.LogicNode successor : successors) {
                if (successor.active)
                    deactivateNode(successor, node.id);
            }
        }
    }

    public class EHBinder extends Binder {
        /**
         * Change the status of a Script node (by operating on its Lotus).
         * The change is guaranteed to be success if the Lotus exists.
         * TODO: Move to {@link CoreServiceComponents.CoreSkillHelper}
         * @param scriptName
         * @param status currently {@code false} only
         * @return {@code true} if this event is listening; {@code false} otherwise
         */
        public boolean setLotusStatus(String scriptName, boolean status) {
            LogicManagedLotus logicManagedLotus = lotusMap.get(scriptName);
            if (logicManagedLotus == null)
                return false;
            logicManagedLotus.setLotusStatus(status);
            return true;
        }

        public Map<String, Boolean> lotusStatusMap() {
            Map<String, Boolean> statusMap = new HashMap<>(lotusMap.size());
            for (Map.Entry<String, LogicManagedLotus> entry : lotusMap.entrySet()) {
                if (entry.getValue().getCount() > 0) {
                    statusMap.put(entry.getKey(), entry.getValue().getLotusStatus());
                }
            }
            return statusMap;
        }
    }

    public class EHTestBinder extends EHBinder {
        public LogicGraph getLogicGraph() {
            return logicGraph;
        }
    }

    public static class LogicManagedLotus {
        //TODO: Merge with lotusMap (and LogicGraph) into LiveLogicGraph

        private final ReentrantLock lck = new ReentrantLock();
        /**
         * Predecessors which are currently satisfied, thus activated this lotus.
         * This may be overkill, because the current logic should already guarantee only one call
         * from one predecessor is done.
         * But in case this is violated (e.g. #414), this replaces the count-based mechanism. This
         * mechanism allows better debugging (logging).
         */
        private final Set<String> livePredecessors = new HashSet<>();
        private final Lotus object;

        public LogicManagedLotus(Lotus lotus) {
            this.object = lotus;
        }

        public int getCount() {
            return livePredecessors.size();
        }

        public void activateFrom(@Nullable String from) {
            lck.lock();
            try {
                if (livePredecessors.contains(from)) {
                    Logger.e("LML %s already activated from %s, but is activating again? Ignored.", object.node.id, from);
                    return;
                }
                livePredecessors.add(from);
                if (getCount() == 1) {
                    object.listen();
                }
            } finally {
                lck.unlock();
            }
        }

        public void deactivateFrom(@Nullable String from) {
            lck.lock();
            try {
                if (!livePredecessors.contains(from)) {
                    Logger.e("LML %s was not activated from %s, but is deactivating? Ignored.", object.node.id, from);
                    return;
                }
                livePredecessors.remove(from);
                if (getCount() == 0) {
                    object.cancel();
                }
            } finally {
                lck.unlock();
            }
        }

        public void setLotusStatus(boolean status) {
            object.setStatus(status);
        }

        public boolean getLotusStatus() {
            return object.status();
        }
    }

}
