/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ryey.easer.core.data.EventTree;
import ryey.easer.core.data.storage.EventDataStorage;

/*
 * The background service which maintains several Lotus(es) and send Intent to load Profile(s).
 */
public class EHService extends Service {
    public static final String ACTION_RELOAD = "ryey.easer.action.RELOAD";

    public static final String ACTION_STATE_CHANGED = "ryey.easer.action.STATE_CHANGED";
    public static final String ACTION_PROFILE_UPDATED = "ryey.easer.action.PROFILE_UPDATED";

    List<Lotus> mLotusArray = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.d("Broadcast received :: action: <%s>", action);
            switch (action) {
                case ACTION_RELOAD:
                    reloadTriggers();
                    break;
                case ProfileLoaderIntentService.ACTION_PROFILE_LOADED:
                    recordProfile(intent.getExtras());
                    Intent intent1 = new Intent();
                    intent1.setAction(ACTION_PROFILE_UPDATED);
                    sendBroadcast(intent1);
                    break;
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

    private static String lastProfile = null;
    private static String fromEvent = null;
    private static long loadTime = -1;

    private static void recordProfile(Bundle bundle) {
        final String profileName = bundle.getString(ProfileLoaderIntentService.EXTRA_PROFILE_NAME);
        final String eventName = bundle.getString(ProfileLoaderIntentService.EXTRA_EVENT_NAME);
        final long time = bundle.getLong(ProfileLoaderIntentService.EXTRA_LOAD_TIME);
        lastProfile = profileName;
        fromEvent = eventName;
        loadTime = time;
    }
    public static String getLastProfile() {
        return lastProfile;
    }
    public static String getFromEvent() {
        return fromEvent;
    }
    public static long getLoadTime() {
        return loadTime;
    }

    @Override
    public void onCreate() {
        Logger.v("onCreate");
        running = true;
        Intent intent = new Intent(ACTION_STATE_CHANGED);
        sendBroadcast(intent);
        lastProfile = null;
        fromEvent = null;
        loadTime = -1;
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RELOAD);
        filter.addAction(ProfileLoaderIntentService.ACTION_PROFILE_LOADED);
        registerReceiver(mReceiver, filter);
        reloadTriggers();
        if (SettingsHelper.passiveMode(this)) {
            for (Lotus lotus : mLotusArray) {
                lotus.check();
            }
        }
        Logger.i("EHService created");
    }

    @Override
    public void onDestroy() {
        Logger.v("onDestroy");
        super.onDestroy();
        mCancelTriggers();
        unregisterReceiver(mReceiver);
        running = false;
        Intent intent = new Intent(ACTION_STATE_CHANGED);
        sendBroadcast(intent);
    }

    private void reloadTriggers() {
        Logger.v("reloadTriggers()");
        mCancelTriggers();
        EventDataStorage storage = EventDataStorage.getInstance(this);
        List<EventTree> events = storage.getEventTrees();
        mSetTriggers(events);
        Logger.d("triggers reloaded");
    }

    private void mCancelTriggers() {
        for (Lotus lotus : mLotusArray) {
            lotus.cancel();
        }
        mLotusArray.clear();
    }

    private void mSetTriggers(List<EventTree> eventTreeList) {
        Logger.v("setting triggers");
        for (EventTree event : eventTreeList) {
            Logger.v("setting trigger for <%s>", event.getName());
            if (event.isActive()) {
                Lotus lotus = new Lotus(this, event, executorService);
                lotus.listen();
                Logger.v("trigger for event <%s> is set", event.getName());
                mLotusArray.add(lotus);
            }
        }
        Logger.d("triggers have been set");
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
            if (lotus.eventTree.getName().equals(eventName)) {
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
