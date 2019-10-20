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
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
     * NEW
     * key: Script (node) name
     * value: lotus
     */
    Map<String, CountedLotus> lotusMap = new HashMap<>(); //TODO: merge into LogicGraph ?
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
                CountedLotus countedLotus = lotusMap.get(node.id);
                if (countedLotus != null && countedLotus.getCount() > 0) {
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
                CountedLotus countedLotus = lotusMap.get(node.id);
                if (countedLotus != null && countedLotus.getCount() > 0) {
                    countedLotus.decCount();
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
                activate(node);
                Logger.v(TAG + "trigger for script node <%s> is set", node.id);
            }
        }
        Logger.d(TAG + "triggers have been set");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new EHBinder();
    }

    @Override
    public void activate(LogicGraph.LogicNode node) {
        CountedLotus countedLotus = lotusMap.get(node.id);
        if (countedLotus == null) {
            countedLotus = new CountedLotus(Lotus.createLotus(this, node, this, jobCH, jobLP));
            lotusMap.put(node.id, countedLotus);
        }
        countedLotus.incCount();
    }

    @Override
    public void deactivate(LogicGraph.LogicNode node) {
        CountedLotus countedLotus = lotusMap.get(node.id);
        if (countedLotus != null)
            countedLotus.decCount();
    }

    @Override
    public void activateSuccessors(LogicGraph.LogicNode node) {
        Set<LogicGraph.LogicNode> successors = logicGraph.successors(node);
        if (successors != null) {
            for (LogicGraph.LogicNode successor : successors) {
                if (successor.active)
                    activate(successor);
            }
        }
    }

    @Override
    public void deactivateSuccessors(LogicGraph.LogicNode node) {
        Set<LogicGraph.LogicNode> successors = logicGraph.successors(node);
        if (successors != null) {
            for (LogicGraph.LogicNode successor : successors) {
                if (successor.active)
                    deactivate(successor);
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
            CountedLotus countedLotus = lotusMap.get(scriptName);
            if (countedLotus == null)
                return false;
            countedLotus.setLotusStatus(status);
            return true;
        }

        public Map<String, Boolean> lotusStatusMap() {
            Map<String, Boolean> statusMap = new HashMap<>(lotusMap.size());
            for (Map.Entry<String, CountedLotus> entry : lotusMap.entrySet()) {
                if (entry.getValue().getCount() > 0) {
                    statusMap.put(entry.getKey(), entry.getValue().getLotusStatus());
                }
            }
            return statusMap;
        }
    }

    public static class CountedLotus {

        private int count = 0;
        private Lotus object;

        public CountedLotus(Lotus lotus) {
            this.object = lotus;
        }

        public int getCount() {
            return count;
        }

        public void incCount() {
            count++;
            if (count == 1) {
                object.listen();
            }
        }

        public void decCount() {
            count--;
            if (count == 0) {
                object.cancel();
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
