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
import android.os.IBinder;
import android.os.PatternMatcher;
import android.support.v4.util.ArraySet;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import ryey.easer.commons.plugindef.conditionplugin.ConditionData;
import ryey.easer.commons.plugindef.conditionplugin.Tracker;
import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.storage.ConditionDataStorage;
import ryey.easer.plugins.PluginRegistry;

public class ConditionHolderService extends Service {

    private static final String ACTION_TRACKER_SATISFIED = "ryey.easer.triggerlotus.action.TRACKER_SATISFIED";
    private static final String ACTION_TRACKER_UNSATISFIED = "ryey.easer.triggerlotus.action.TRACKER_UNSATISFIED";
    private static final String CATEGORY_NOTIFY_HOLDER = "ryey.easer.triggerlotus.category.NOTIFY_HOLDER";

    //FIXME concurrent
    private Map<String, Tracker> trackerMap = new HashMap<>();
    private Map<String, Set<Lotus.NotifyPendingIntents>> associateMap = new HashMap<>();

    private final Uri uri = Uri.parse(String.format(Locale.US, "conditionholder://%d/", hashCode()));

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (ACTION_TRACKER_SATISFIED.equals(intent.getAction()) || ACTION_TRACKER_UNSATISFIED.equals(intent.getAction())) {
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

    public ConditionHolderService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(mReceiver, filter);
        setTrackers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        cancelTrackers();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new CHBinder();
    }

    private void setTrackers() {
        ConditionDataStorage conditionDataStorage = ConditionDataStorage.getInstance(this);
        for (String name : conditionDataStorage.list()) {
            Intent intent = new Intent(ACTION_TRACKER_SATISFIED);
            Uri turi = uri.buildUpon().appendPath(name).build();
            intent.addCategory(CATEGORY_NOTIFY_HOLDER);
            intent.setData(turi);
            PendingIntent positive = PendingIntent.getBroadcast(this, 0, intent, 0);
            intent.setAction(ACTION_TRACKER_UNSATISFIED);
            PendingIntent negative = PendingIntent.getBroadcast(this, 0, intent, 0);

            ConditionStructure conditionStructure = conditionDataStorage.get(name);
            ConditionData conditionData = conditionStructure.getData();
            Tracker tracker = PluginRegistry.getInstance().condition().findPlugin(conditionData)
                    .tracker(this, conditionData, positive, negative);
            tracker.start();
            trackerMap.put(name, tracker);
            associateMap.put(name, new ArraySet<Lotus.NotifyPendingIntents>());
        }
    }

    private void cancelTrackers() {
        for (Tracker tracker : trackerMap.values()) {
            tracker.stop();
        }
        trackerMap.clear();
        associateMap.clear();
    }

    class CHBinder extends Binder {
        void registerAssociation(String conditionName, Lotus.NotifyPendingIntents pendingIntents) {
            associateMap.get(conditionName).add(pendingIntents);
        }
        void unregisterAssociation(String conditionName, Lotus.NotifyPendingIntents pendingIntents) {
            associateMap.get(conditionName).remove(pendingIntents);
        }
        void clearAssociation() {
            for (String name :associateMap.keySet()) {
                associateMap.get(name).clear();
            }
        }
        Boolean conditionState(String conditionName) {
            Tracker tracker = trackerMap.get(conditionName);
            return tracker.state();
        }
        void reload() {
            //TODO: fine-grained cancel and reset
            cancelTrackers();
            setTrackers();
        }
    }
}
