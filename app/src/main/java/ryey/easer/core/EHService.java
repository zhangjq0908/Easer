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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PatternMatcher;
import android.support.v4.util.ArraySet;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ryey.easer.commons.plugindef.conditionplugin.ConditionData;
import ryey.easer.commons.plugindef.conditionplugin.Tracker;
import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.ScriptTree;
import ryey.easer.core.data.storage.ConditionDataStorage;
import ryey.easer.core.data.storage.ScriptDataStorage;
import ryey.easer.plugins.PluginRegistry;

/*
 * The background service which maintains several Lotus(es) and send Intent to load Profile(s).
 */
public class EHService extends Service {
    public static final String ACTION_RELOAD = "ryey.easer.action.RELOAD";

    public static final String ACTION_STATE_CHANGED = "ryey.easer.action.STATE_CHANGED";
    public static final String ACTION_PROFILE_UPDATED = "ryey.easer.action.PROFILE_UPDATED";

    List<Lotus> mLotusArray = new ArrayList<>();
    ConditionHolder conditionHolder = new ConditionHolder(this);
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
        Logger.v("EHService onCreate()");
        running = true;
        Intent intent = new Intent(ACTION_STATE_CHANGED);
        sendBroadcast(intent);
        eventHistoryRecordList.clear();
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RELOAD);
        filter.addAction(ProfileLoaderIntentService.ACTION_PROFILE_LOADED);
        registerReceiver(mReceiver, filter);
        reloadTriggers();
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
        if (mLotusArray.size() > 0)
            mCancelTriggers();
        ScriptDataStorage storage = ScriptDataStorage.getInstance(this);
        List<ScriptTree> events = storage.getScriptTrees();
        mSetTriggers(events);
        Logger.d("triggers reloaded");
    }

    private void mCancelTriggers() {
        for (Lotus lotus : mLotusArray) {
            lotus.cancel();
        }
        mLotusArray.clear();
        conditionHolder.clear();
    }

    private void mSetTriggers(List<ScriptTree> scriptTreeList) {
        Logger.v("setting triggers");
        conditionHolder.start(scriptTreeList);
        for (ScriptTree script : scriptTreeList) {
            Logger.v("setting trigger for <%s>", script.getName());
            if (script.isActive()) {
                Lotus lotus = Lotus.createLotus(this, script, executorService, conditionHolder);
                lotus.listen();
                Logger.v("trigger for event <%s> is set", script.getName());
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

    static class ConditionHolder {
        private static final String ACTION_TRACKER_SATISFIED = "ryey.easer.triggerlotus.action.TRACKER_SATISFIED";
        private static final String ACTION_TRACKER_UNSATISFIED = "ryey.easer.triggerlotus.action.TRACKER_UNSATISFIED";
        private static final String CATEGORY_NOTIFY_HOLDER = "ryey.easer.triggerlotus.category.NOTIFY_HOLDER";

        private final Context context;

        private Map<String, Tracker> trackerMap;
        private Map<String, Set<Lotus.NotifyPendingIntents>> associateMap;
        
        private final Uri uri = Uri.parse(String.format(Locale.US, "conditionholder://%d/", hashCode()));

        private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String name = intent.getData().getLastPathSegment();
                    if (intent.getAction().equals(ACTION_TRACKER_SATISFIED)) {
                        for (Lotus.NotifyPendingIntents pendingIntents : associateMap.get(name)) {
                            try {
                                pendingIntents.positive.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (intent.getAction().equals(ACTION_TRACKER_UNSATISFIED)) {
                        for (Lotus.NotifyPendingIntents pendingIntents : associateMap.get(name)) {
                            try {
                                pendingIntents.negative.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    Logger.e(e, "ConditionHolder's BroadcastListener shouldn't hear invalid Intent");
                }
            }
        };
        private final IntentFilter filter;

        {
            filter = new IntentFilter();
            filter.addAction(ACTION_TRACKER_SATISFIED);
            filter.addAction(ACTION_TRACKER_UNSATISFIED);
            filter.addCategory(CATEGORY_NOTIFY_HOLDER);
            filter.addDataScheme(uri.getScheme());
            filter.addDataAuthority(uri.getAuthority(), null);
            filter.addDataPath(uri.getPath(), PatternMatcher.PATTERN_PREFIX);
        }

        private ConditionHolder(Context context) {
            this.context = context;
            trackerMap = new HashMap<>();
            associateMap = new HashMap<>();
        }

        private synchronized void start(List<ScriptTree> scriptTreeList) {
            context.registerReceiver(mReceiver, filter);
            ConditionDataStorage conditionDataStorage = ConditionDataStorage.getInstance(context);
            for (String name : conditionsInUse(scriptTreeList)) {
                Intent intent = new Intent(ACTION_TRACKER_SATISFIED);
                Uri turi = uri.buildUpon().appendPath(name).build();
                intent.addCategory(CATEGORY_NOTIFY_HOLDER);
                intent.setData(turi);
                PendingIntent positive = PendingIntent.getBroadcast(context, 0, intent, 0);
                intent.setAction(ACTION_TRACKER_UNSATISFIED);
                PendingIntent negative = PendingIntent.getBroadcast(context, 0, intent, 0);

                ConditionStructure conditionStructure = conditionDataStorage.get(name);
                ConditionData conditionData = conditionStructure.getData();
                Tracker tracker = PluginRegistry.getInstance().condition().findPlugin(conditionData)
                        .tracker(context, conditionData, positive, negative);
                tracker.start();
                trackerMap.put(name, tracker);
                associateMap.put(name, new ArraySet<Lotus.NotifyPendingIntents>());
            }
        }
        private synchronized void clear() {
            context.unregisterReceiver(mReceiver);
            for (Tracker tracker : trackerMap.values()) {
                tracker.stop();
            }
            trackerMap.clear();
            associateMap.clear();
        }

        void registerAssociation(String conditionName, Lotus.NotifyPendingIntents pendingIntents) {
            associateMap.get(conditionName).add(pendingIntents);
        }
        void unregisterAssociation(String conditionName, Lotus.NotifyPendingIntents pendingIntents) {
            associateMap.get(conditionName).remove(pendingIntents);
        }
        Boolean conditionState(String conditionName) {
            Tracker tracker = trackerMap.get(conditionName);
            return tracker.state();
        }

        private static Set<String> conditionsInUse(List<ScriptTree> scriptTreeList) {
            Set<String> names = new ArraySet<>();
            for (ScriptTree script : scriptTreeList) {
                if (script.isActive()) {
                    if (script.isCondition()) {
                        names.add(script.getCondition().getName());
                    }
                    names.addAll(conditionsInUse(script.getSubs()));
                }
            }
            return names;
        }
    }
}
