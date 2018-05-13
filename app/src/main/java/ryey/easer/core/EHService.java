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

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ryey.easer.core.data.ScriptTree;
import ryey.easer.core.data.storage.ScriptDataStorage;

/*
 * The background service which maintains several Lotus(es) and send Intent to load Profile(s).
 */
public class EHService extends Service {
    public static final String ACTION_RELOAD = "ryey.easer.action.RELOAD";

    public static final String ACTION_STATE_CHANGED = "ryey.easer.action.STATE_CHANGED";
    public static final String ACTION_PROFILE_UPDATED = "ryey.easer.action.PROFILE_UPDATED";


    private static final String ACTION_UNREGISTER_CONDITION_EVENT = "ryey.easer.service.action.UNREGISTER_CONDITION_EVENT";
    private static final String ACTION_REGISTER_CONDITION_EVENT = "ryey.easer.service.action.REGISTER_CONDITION_EVENT";
    private static final String EXTRA_CONDITION_NAME = "ryey.easer.service.extra.CONDITION_NAME";
    private static final String EXTRA_PENDING_INTENT = "ryey.easer.service.extra.PENDING_INTENT";
    private static final IntentFilter filter_conditionEvent;
    static {
        filter_conditionEvent = new IntentFilter();
        filter_conditionEvent.addAction(ACTION_REGISTER_CONDITION_EVENT);
        filter_conditionEvent.addAction(ACTION_UNREGISTER_CONDITION_EVENT);
    }
    public static void registerConditionEventNotification(@NonNull Context context, @NonNull String conditionName, @NonNull PendingIntent[] pendingIntents) {
        Intent intent = new Intent(ACTION_REGISTER_CONDITION_EVENT);
        intent.putExtra(EXTRA_CONDITION_NAME, conditionName);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntents);
        context.sendBroadcast(intent);
    }
    public static void unregisterConditionEventNotification(@NonNull Context context, @NonNull String conditionName, @NonNull PendingIntent[] pendingIntents) {
        Intent intent = new Intent(ACTION_UNREGISTER_CONDITION_EVENT);
        intent.putExtra(EXTRA_CONDITION_NAME, conditionName);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntents);
        context.sendBroadcast(intent);
    }

    private static final String TAG = "[EHService] ";

    List<Lotus> mLotusArray = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    ConditionHolderService.CHBinder conditionHolderBinder;
    private final ConditionVariable cv = new ConditionVariable();
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Logger.v(TAG + "onServiceConnected");
            conditionHolderBinder = (ConditionHolderService.CHBinder) iBinder;
            cv.open();
            mSetTriggers();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Logger.v(TAG + "onServiceDisconnected");
            cv.close();
            conditionHolderBinder = null;
        }
    };

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.d("Broadcast received :: action: <%s>", action);
            if (ACTION_RELOAD.equals(action)) {
                reloadTriggers();
            } else if (ProfileLoaderIntentService.ACTION_PROFILE_LOADED.equals(action)) {
                recordProfile(intent.getExtras());
                Intent intent1 = new Intent();
                intent1.setAction(ACTION_PROFILE_UPDATED);
                sendBroadcast(intent1);
            } else if (ACTION_REGISTER_CONDITION_EVENT.equals(intent.getAction()) || ACTION_UNREGISTER_CONDITION_EVENT.equals(intent.getAction())) {
                String conditionName = intent.getStringExtra(EXTRA_CONDITION_NAME);
                PendingIntent []pendingIntents= (PendingIntent[]) intent.getParcelableArrayExtra(EXTRA_PENDING_INTENT);
                Lotus.NotifyPendingIntents notifyPendingIntents = new Lotus.NotifyPendingIntents(pendingIntents[0], pendingIntents[1]);
                requireCHService(TAG);
                if (ACTION_REGISTER_CONDITION_EVENT.equals(intent.getAction()))
                    conditionHolderBinder.registerAssociation(conditionName, notifyPendingIntents);
                else
                    conditionHolderBinder.unregisterAssociation(conditionName, notifyPendingIntents);
            }
        }
    };

    private static boolean running = false;

    public static boolean isRunning() {
        return running;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EHService.class);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, EHService.class);
        context.stopService(intent);
    }

    public static void reload(Context context) {
        Intent intent = new Intent();
        intent.setAction(EHService.ACTION_RELOAD);
        context.sendBroadcast(intent);
    }

    private static LinkedList<EventHistoryRecord> eventHistoryRecordList = new LinkedList<>();

    synchronized private static void recordProfile(Bundle bundle) {
        final String profileName = bundle.getString(ProfileLoaderIntentService.EXTRA_PROFILE_NAME);
        final String eventName = bundle.getString(ProfileLoaderIntentService.EXTRA_EVENT_NAME);
        final long time = bundle.getLong(ProfileLoaderIntentService.EXTRA_LOAD_TIME);
        if (eventHistoryRecordList.size() > 1000)
            eventHistoryRecordList.removeFirst();
        eventHistoryRecordList.addLast(new EventHistoryRecord(eventName, profileName, time));
    }
    public static EventHistoryRecord getLastHistory() {
        if (eventHistoryRecordList.size() == 0)
            return null;
        return eventHistoryRecordList.getLast();
    }
    public static List<EventHistoryRecord> getHistory() {
        return eventHistoryRecordList;
    }

    @Override
    public void onCreate() {
        Logger.v(TAG + "onCreate()");
        super.onCreate();
        bindService(new Intent(this, ConditionHolderService.class), connection, Context.BIND_AUTO_CREATE);
        running = true;
        Intent intent = new Intent(ACTION_STATE_CHANGED);
        sendBroadcast(intent);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RELOAD);
        filter.addAction(ProfileLoaderIntentService.ACTION_PROFILE_LOADED);
        registerReceiver(mReceiver, filter);
        registerReceiver(mReceiver, filter_conditionEvent);
        Logger.i(TAG + "created");
    }

    @Override
    public void onDestroy() {
        Logger.v(TAG + "onDestroy");
        super.onDestroy();
        mCancelTriggers();
        unregisterReceiver(mReceiver);
        unbindService(connection);
        running = false;
        Intent intent = new Intent(ACTION_STATE_CHANGED);
        sendBroadcast(intent);
        Logger.i(TAG + "destroyed");
    }

    private void reloadTriggers() {
        Logger.v(TAG + "reloadTriggers()");
        if (mLotusArray.size() > 0)
            mCancelTriggers();
        mSetTriggers();
        Logger.d(TAG + "triggers reloaded");
    }

    private void mCancelTriggers() {
        for (Lotus lotus : mLotusArray) {
            lotus.cancel();
        }
        mLotusArray.clear();
        requireCHService(TAG);
        conditionHolderBinder.clearAssociation();
    }

    private void mSetTriggers() {
        final String TAG = "[EHService:mSetTriggers] ";
        Logger.v(TAG + "setting triggers");
        ScriptDataStorage storage = ScriptDataStorage.getInstance(this);
        List<ScriptTree> scriptTreeList = storage.getScriptTrees();
        requireCHService(TAG);
        for (ScriptTree script : scriptTreeList) {
            Logger.v(TAG + "setting trigger for <%s>", script.getName());
            if (script.isActive()) {
                Lotus lotus = Lotus.createLotus(this, script, executorService, conditionHolderBinder);
                lotus.listen();
                Logger.v(TAG + "trigger for event <%s> is set", script.getName());
                mLotusArray.add(lotus);
            }
        }
        Logger.d(TAG + "triggers have been set");
    }

    private void requireCHService(String TAG) {
        Logger.v(TAG + "waiting for ConditionVariable for CHBinder");
        cv.block();
        Logger.v(TAG + "ConditionVariable for CHBinder met");
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
    }

}
