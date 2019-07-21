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

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ryey.easer.R;
import ryey.easer.SettingsUtils;
import ryey.easer.core.data.ScriptTree;
import ryey.easer.core.data.storage.ScriptDataStorage;
import ryey.easer.core.log.ActivityLogService;
import ryey.easer.core.ui.MainActivity;
import ryey.easer.skills.event.widget.UserActionWidget;

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
        context.sendBroadcast(intent);
        //TODO local broadcast
    }
    public static void unregisterConditionEventNotifier(@NonNull Context context, @NonNull String conditionName, @NonNull Uri notifyData) {
        Intent intent = new Intent(ACTION_UNREGISTER_CONDITION_EVENT);
        intent.putExtra(EXTRA_CONDITION_NAME, conditionName);
        intent.putExtra(EXTRA_NOTIFY_DATA, notifyData);
        context.sendBroadcast(intent);
        //TODO local broadcast
    }

    private static final String TAG = "[EHService] ";
    private static final String SERVICE_NAME = "Easer";
    private static final int NOTIFICATION_ID = 1;

    List<Lotus> mLotusArray = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    ConditionHolderService.CHBinder conditionHolderBinder;
    private final ConditionVariable cv = new ConditionVariable();
    private final AsyncHelper.DelayedLoadProfileJobs jobContainerLP = new AsyncHelper.DelayedLoadProfileJobs();
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Logger.v("%s onServiceConnected: %s", TAG, componentName);
            if (componentName.getClassName().equals(ConditionHolderService.class.getName())) {
                conditionHolderBinder = (ConditionHolderService.CHBinder) iBinder;
                cv.open();
                mSetTriggers();
            } else if (componentName.getClassName().equals(ProfileLoaderService.class.getName())) {
                jobContainerLP.onBind((ProfileLoaderService.PLSBinder) iBinder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Logger.v("%s onServiceDisconnected: %s", TAG, componentName);
            if (componentName.getClassName().equals(ConditionHolderService.class.getName())) {
                cv.close();
                conditionHolderBinder = null;
            } else if (componentName.getClassName().equals(ProfileLoaderService.class.getName())) {
                jobContainerLP.onUnbind();
            }
        }
    };


    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.d("Broadcast received :: action: <%s>", action);
            if (ACTION_RELOAD.equals(action)) {
                reloadTriggers();
            } else if (ACTION_REGISTER_CONDITION_EVENT.equals(intent.getAction()) || ACTION_UNREGISTER_CONDITION_EVENT.equals(intent.getAction())) {
                String conditionName = intent.getStringExtra(EXTRA_CONDITION_NAME);
                Uri notifyData = intent.getParcelableExtra(EXTRA_NOTIFY_DATA);
                requireCHService(TAG);
                if (ACTION_REGISTER_CONDITION_EVENT.equals(intent.getAction()))
                    conditionHolderBinder.registerAssociation(conditionName, notifyData);
                else
                    conditionHolderBinder.unregisterAssociation(conditionName, notifyData);
            }
        }
    };

    WidgetBroadcastRedispatcher widgetBroadcastRedispatcher = new WidgetBroadcastRedispatcher();

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
        context.sendBroadcast(intent);
    }

    private void startNotification() {
        if (!SettingsUtils.showNotification(this))
            return;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "easer_ind";
            String channelName = "Easer Service Indicator";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(this, channelId);
            builder.setAutoCancel(true);
        } else {
            builder = new NotificationCompat.Builder(this)
                    .setPriority(NotificationCompat.PRIORITY_MIN);
        }
        final int REQ_CODE = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, REQ_CODE, new Intent(this, MainActivity.class), 0);
        builder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getString(
                        R.string.text_notification_running_indicator_content,
                        getString(R.string.easer)))
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setContentIntent(pendingIntent);

        Notification indicatorNotification = builder.build();

        if (SettingsUtils.runInForeground(this)) {
            startForeground(NOTIFICATION_ID, indicatorNotification);
        } else {
            notificationManager.notify(NOTIFICATION_ID, indicatorNotification);
        }
    }

    private void stopNotification() {
        if (!SettingsUtils.showNotification(this))
            return;
        if (SettingsUtils.runInForeground(this)) {

        } else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    @Override
    public void onCreate() {
        Logger.v(TAG + "onCreate()");
        super.onCreate();
        widgetBroadcastRedispatcher.start(this);
        startNotification();
        ActivityLogService.Companion.notifyServiceStatus(this, SERVICE_NAME, true, null);
        bindService(new Intent(this, ConditionHolderService.class), connection, Context.BIND_AUTO_CREATE);
        bindService(new Intent(this, ProfileLoaderService.class), connection, Context.BIND_AUTO_CREATE);
        running = true;
        Intent intent = new Intent(ACTION_STATE_CHANGED);
        sendBroadcast(intent);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RELOAD);
        registerReceiver(mReceiver, filter);
        registerReceiver(mReceiver, filter_conditionEvent);
        Logger.i(TAG + "created");
    }

    @Override
    public void onDestroy() {
        Logger.v(TAG + "onDestroy");
        super.onDestroy();
        widgetBroadcastRedispatcher.stop(this);
        stopNotification();
        ActivityLogService.Companion.notifyServiceStatus(this, SERVICE_NAME, false, null);
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
        conditionHolderBinder.reload();
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
        ScriptDataStorage storage = new ScriptDataStorage(this);
        List<ScriptTree> scriptTreeList = storage.getScriptTrees();
        requireCHService(TAG);
        for (ScriptTree script : scriptTreeList) { //TODO?: Move this to `FakeRootLotus`
            Logger.v(TAG + "setting trigger for <%s>", script.getName());
            if (script.isActive()) {
                Lotus lotus = Lotus.createLotus(this, script, executorService, conditionHolderBinder, jobContainerLP);
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

        public List<Lotus.Status> lotusStatus() {
            List<Lotus.Status> statusList = new LinkedList<>();
            for (Lotus lotus : mLotusArray) {
                statusList.addAll(lotus.statusRec());
            }
            return statusList;
        }
    }

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
