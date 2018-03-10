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

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

import ryey.easer.SettingsHelper;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.core.data.EventTree;
import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.plugins.PluginRegistry;

/*
 * Each Lotus holds one EventTree.
 *
 * The root of that tree will be registered listener.
 * When the root is satisfied, Lotus will recursively check all children to find which is satisfied.
 * After finding a satisfied child and the child has a Profile, Lotus will load the Profile.
 *
 * See onSlotSatisfied() for the actual way to check and determine which is satisfied.
 */
final class Lotus {
    private static final String ACTION_SLOT_SATISFIED = "ryey.easer.triggerlotus.action.SLOT_SATISFIED";
    private static final String ACTION_SLOT_UNSATISFIED = "ryey.easer.triggerlotus.action.SLOT_UNSATISFIED";
    private static final String CATEGORY_NOTIFY_LOTUS = "ryey.easer.triggerlotus.category.NOTIFY_LOTUS";

    private Context context;
    final EventTree eventTree;
    private final ExecutorService executorService;

    private final Uri uri = Uri.parse(String.format(Locale.US, "lotus://%d", hashCode()));
    private final PendingIntent notifyLotusIntent_positive, notifyLotusIntent_negative;

    private AbstractSlot mSlot;
    List<Lotus> subs = new ArrayList<>();

    private final long cooldownInMillisecond;
    private Calendar lastSatisfied;

    private final boolean repeatable;
    private final boolean persistent;
    private boolean satisfied = false;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SLOT_SATISFIED)) {
                onSlotSatisfied();
            } else if (intent.getAction().equals(ACTION_SLOT_UNSATISFIED)) {
                onSlotUnsatisfied();
            }
        }
    };
    private final IntentFilter filter;

    {
        filter = new IntentFilter();
        filter.addAction(ACTION_SLOT_SATISFIED);
        filter.addAction(ACTION_SLOT_UNSATISFIED);
        filter.addCategory(CATEGORY_NOTIFY_LOTUS);
        filter.addDataScheme(uri.getScheme());
        filter.addDataAuthority(uri.getAuthority(), null);
        filter.addDataPath(uri.getPath(), PatternMatcher.PATTERN_LITERAL);
    }

    Lotus(Context context, EventTree eventTree, ExecutorService executorService) {
        this.context = context;
        this.eventTree = eventTree;
        this.executorService = executorService;

        Intent intent1 = new Intent(ACTION_SLOT_SATISFIED);
        intent1.addCategory(CATEGORY_NOTIFY_LOTUS);
        intent1.setData(uri);
        notifyLotusIntent_positive = PendingIntent.getBroadcast(context, 0, intent1, 0);
        intent1.setAction(ACTION_SLOT_UNSATISFIED);
        notifyLotusIntent_negative = PendingIntent.getBroadcast(context, 0, intent1, 0);

        mSlot = nodeToSlot(eventTree);
        if (eventTree.isReversed()) {
            mSlot.register(notifyLotusIntent_negative, notifyLotusIntent_positive);
        } else {
            mSlot.register(notifyLotusIntent_positive, notifyLotusIntent_negative);
        }
        repeatable = eventTree.isRepeatable();
        persistent = eventTree.isPersistent();

        cooldownInMillisecond = SettingsHelper.coolDownInterval(context);
    }

    private <T extends EventData> AbstractSlot<T> nodeToSlot(EventTree node) {
        ScenarioStructure scenario = node.getScenario();
        AbstractSlot<T> slot;
        //noinspection unchecked
        T data = (T) scenario.getEventData();
        //noinspection unchecked
        EventPlugin<T> plugin = PluginRegistry.getInstance().event().findPlugin(data);
        if (scenario.isTmpScenario()) {
            slot = plugin.slot(context, data);
        } else {
            slot = plugin.slot(context, data, repeatable, persistent);
        }
        return slot;
    }

    synchronized void check() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mSlot.check();
            }
        });
    }

    synchronized void listen() {
        context.registerReceiver(mReceiver, filter);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mSlot.listen();
            }
        });
    }

    synchronized void cancel() {
        context.unregisterReceiver(mReceiver);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mSlot.cancel();
                for (Lotus sub : subs) {
                    sub.cancel();
                }
                subs.clear();
            }
        });
    }

    /**
     * Dirty hack for {@link ryey.easer.plugins.operation.event_control.EventControlOperationPlugin}
     * TODO: cleaner solution
     * @param status new status for the top level slot of this lotus
     */
    synchronized void setStatus(boolean status) {
        if (status) {
            onSlotSatisfied();
        } else {
            onSlotUnsatisfied();
        }
    }

    private boolean checkAndSetCooldown(String eventName) {
        if (cooldownInMillisecond > 0) {
            Calendar now = Calendar.getInstance();
            if (lastSatisfied != null) {
                if (now.getTimeInMillis() - lastSatisfied.getTimeInMillis() < cooldownInMillisecond) {
                    Logger.d("event <%s> is within cooldown time", eventName);
                    return false;
                }
            }
            Logger.d("event <%s> is not within cooldown time", eventName);
            lastSatisfied = now;
            return true;
        }
        return true;
    }

    private synchronized void onSlotSatisfied() {
        if (!repeatable && satisfied)
            return;
        if (checkAndSetCooldown(eventTree.getName())) {
            Logger.i("event <%s> satisfied", eventTree.getName());
            satisfied = true;
            triggerAndPromote();
        }
    }

    private synchronized void onSlotUnsatisfied() {
        if (persistent && satisfied)
            return;
        if (checkAndSetCooldown(eventTree.getName())) {
            Logger.i("Event %s unsatisfied", eventTree.getName());
            satisfied = false;
            for (Lotus sub : subs) {
                sub.cancel();
            }
            subs.clear();
        }
    }

    private void triggerAndPromote() {
        Logger.v(" traversing and find <%s> satisfied", eventTree.getName());
        String profileName = eventTree.getProfile();
        if (profileName != null) {
            Intent intent = new Intent(context, ProfileLoaderIntentService.class);
            intent.setAction(ProfileLoaderIntentService.ACTION_LOAD_PROFILE);
            intent.putExtra(ProfileLoaderIntentService.EXTRA_PROFILE_NAME, profileName);
            intent.putExtra(ProfileLoaderIntentService.EXTRA_EVENT_NAME, eventTree.getName());
            context.startService(intent);
        }
        for (EventTree sub : eventTree.getSubs()) {
            if (sub.isActive()) {
                Lotus subLotus = new Lotus(context, sub, executorService);
                subs.add(subLotus);
                subLotus.listen();
                if (!SettingsHelper.passiveMode(context)) {
                    subLotus.check();
                }
            }
        }
    }
}
