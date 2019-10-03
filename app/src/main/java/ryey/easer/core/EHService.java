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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ryey.easer.core.data.ScriptTree;
import ryey.easer.core.data.storage.ScriptDataStorage;
import ryey.easer.core.log.ActivityLogService;
import ryey.easer.skills.event.widget.UserActionWidget;

/**
 * The main background service.
 * It maintains several {@link Lotus}(es) and send Intent to load Profile(s) & etc.
 */
public class EHService extends Service {
    public static final String ACTION_RELOAD = "ryey.easer.action.RELOAD";

    public static final String ACTION_STATE_CHANGED = "ryey.easer.action.STATE_CHANGED";
    public static final String ACTION_PROFILE_UPDATED = "ryey.easer.action.PROFILE_UPDATED";

    private static final String TAG = "[EHService] ";
    private static final String SERVICE_NAME = "Easer";

    /**
     * All 1-layer {@link Lotus}es are stored here.
     * TODO: Store all of them here (with additional helper function)
     */
    List<Lotus> mLotusArray = new ArrayList<>();
    /**
     * Shared thread pool for all {@link Lotus} to execute tasks
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    /**
     * Necessary objects for the correct functioning of EHService.
     * {@link #jobCH} is for calling functions from {@link ConditionHolderService}
     * {@link #jobLP} is for calling functions from {@link ProfileLoaderService}
     */
    private final DelayedConditionHolderBinderJobs jobCH = new DelayedConditionHolderBinderJobs();
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

    private final CoreSkillHelper coreSkillHelper = new CoreSkillHelper(this);
    public static void registerConditionEventNotifier(@NonNull Context context, @NonNull String conditionName, @NonNull Uri notifyData) {
        CoreSkillHelper.registerConditionEventNotifier(context, conditionName, notifyData);
    }
    public static void unregisterConditionEventNotifier(@NonNull Context context, @NonNull String conditionName, @NonNull Uri notifyData) {
        CoreSkillHelper.unregisterConditionEventNotifier(context, conditionName, notifyData);
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
            if (mLotusArray.size() > 0)
                mCancelTriggers();
            mSetTriggers();
            Logger.d(TAG + "triggers reloaded");
        });
    }

    private void mCancelTriggers() {
        jobCH.doAfter(binder -> {
            for (Lotus lotus : mLotusArray) {
                lotus.cancel();
            }
            mLotusArray.clear();
            binder.clearAssociation();
        });
    }

    private void mSetTriggers() {
        final String TAG = "[EHService:mSetTriggers] ";
        Logger.v(TAG + "setting triggers");
        ScriptDataStorage storage = new ScriptDataStorage(this);
        List<ScriptTree> scriptTreeList = storage.getScriptTrees();
        for (ScriptTree script : scriptTreeList) { //TODO?: Move this to `FakeRootLotus`
            Logger.v(TAG + "setting trigger for <%s>", script.getName());
            if (script.isActive()) {
                Lotus lotus = Lotus.createLotus(this, script, executorService, jobCH, jobLP);
                lotus.listen();
                Logger.v(TAG + "trigger for event <%s> is set", script.getName());
                mLotusArray.add(lotus);
            }
        }
        Logger.d(TAG + "triggers have been set");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new EHBinder();
    }

    public class EHBinder extends Binder {
        /**
         * Change the status of an event (by operating on its Lotus).
         * The change is guaranteed to be success if the Lotus exists.
         * @param eventName
         * @param status currently {@code false} only
         * @return {@code true} if this event is listening; {@code false} otherwise
         */
        public boolean setLotusStatus(String eventName, boolean status) {
            for (Lotus lotus : mLotusArray) {
                if (setLotusStatus_real(lotus, eventName, status))
                    return true;
            }
            return false;
        }

        private boolean setLotusStatus_real(Lotus lotus, String eventName, boolean status) {
            if (lotus.scriptName().equals(eventName)) {
                lotus.setStatus(status);
                return true;
            } else {
                for (Lotus sub : lotus.subs) {
                    if (setLotusStatus_real(sub, eventName, status))
                        return true;
                }
            }
            return false;
        }

        public List<Lotus.Status> lotusStatus() {
            List<Lotus.Status> statusList = new LinkedList<>();
            for (Lotus lotus : mLotusArray) {
                statusList.addAll(lotus.statusRec());
            }
            return statusList;
        }
    }

    static class DelayedConditionHolderBinderJobs extends AsyncHelper.DelayedServiceBinderJobs<ConditionHolderService.CHBinder> {

    }

    private static class CoreSkillHelper {
        private static final String ACTION_UNREGISTER_CONDITION_EVENT = "ryey.easer.service.action.UNREGISTER_CONDITION_EVENT";
        private static final String ACTION_REGISTER_CONDITION_EVENT = "ryey.easer.service.action.REGISTER_CONDITION_EVENT";
        private static final String EXTRA_CONDITION_NAME = "ryey.easer.service.extra.CONDITION_NAME";
        private static final String EXTRA_NOTIFY_DATA = "ryey.easer.service.extra.NOTIFY_DATA";
        private static final IntentFilter filter_conditionEvent;
        static {
            filter_conditionEvent = new IntentFilter();
            filter_conditionEvent.addAction(ACTION_REGISTER_CONDITION_EVENT);
            filter_conditionEvent.addAction(ACTION_UNREGISTER_CONDITION_EVENT);
        }

        public static void registerConditionEventNotifier(@NonNull Context context, @NonNull String conditionName, @NonNull Uri notifyData) {
            Intent intent = new Intent(ACTION_REGISTER_CONDITION_EVENT);
            intent.putExtra(EXTRA_CONDITION_NAME, conditionName);
            intent.putExtra(EXTRA_NOTIFY_DATA, notifyData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
        public static void unregisterConditionEventNotifier(@NonNull Context context, @NonNull String conditionName, @NonNull Uri notifyData) {
            Intent intent = new Intent(ACTION_UNREGISTER_CONDITION_EVENT);
            intent.putExtra(EXTRA_CONDITION_NAME, conditionName);
            intent.putExtra(EXTRA_NOTIFY_DATA, notifyData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        /**
         * For {@link ryey.easer.skills.event.condition_event.ConditionEventEventSkill}
         */
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Logger.d("Broadcast received :: action: <%s>", action);
                if (ACTION_REGISTER_CONDITION_EVENT.equals(intent.getAction()) || ACTION_UNREGISTER_CONDITION_EVENT.equals(intent.getAction())) {
                    String conditionName = intent.getStringExtra(EXTRA_CONDITION_NAME);
                    Uri notifyData = intent.getParcelableExtra(EXTRA_NOTIFY_DATA);
                    if (ACTION_REGISTER_CONDITION_EVENT.equals(intent.getAction()))
                        service.jobCH.doAfter(binder -> binder.registerAssociation(conditionName, notifyData));
                    else
                        service.jobCH.doAfter(binder -> binder.unregisterAssociation(conditionName, notifyData));
                }
            }
        };

        private final EHService service;

        private CoreSkillHelper(EHService service) {
            this.service = service;
        }

        /**
         * For {@link ryey.easer.skills.event.widget.WidgetEventSkill}
         */
        WidgetBroadcastRedispatcher widgetBroadcastRedispatcher = new WidgetBroadcastRedispatcher();

        void onCreate() {
            widgetBroadcastRedispatcher.start(service);
            LocalBroadcastManager.getInstance(service).registerReceiver(mReceiver, filter_conditionEvent);
        }

        void onDestroy() {
            widgetBroadcastRedispatcher.stop(service);
            LocalBroadcastManager.getInstance(service).unregisterReceiver(mReceiver);
        }

        /**
         * For {@link ryey.easer.skills.event.widget.WidgetEventSkill}
         */
        private static class WidgetBroadcastRedispatcher {
            /**
             * Handles the broadcast (from PendingIntent) from any widgets, by redispatching
             * This is required because AppWidget can only send PendingIntent to a specific Component (EHService)
             */
            final BroadcastReceiver widgetBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            };

            void start(Context context) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(UserActionWidget.Companion.getACTION_WIDGET_CLICKED());
                context.registerReceiver(widgetBroadcastReceiver, intentFilter);
            }

            void stop(Context context) {
                context.unregisterReceiver(widgetBroadcastReceiver);
            }
        }
    }

}
