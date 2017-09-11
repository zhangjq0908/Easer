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
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.core.data.EventTree;
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
    private final EventTree eventTree;

    private final Uri uri = Uri.parse(String.format("lotus://%d", hashCode()));
    private final PendingIntent notifyLotusIntent, notifyLotusUnsatisfiedIntent;

    private AbstractSlot mSlot;
    private List<Lotus> subs = new ArrayList<>();

    private boolean analyzing = false;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SLOT_SATISFIED)) {
                onSlotSatisfied();
            } else if (intent.getAction().equals(ACTION_SLOT_UNSATISFIED)) {
                onSlotUnsatisfied();
            }
        }
    };

    Lotus(Context context, EventTree eventTree) {
        this.context = context;
        this.eventTree = eventTree;

        Intent intent1 = new Intent(ACTION_SLOT_SATISFIED);
        intent1.addCategory(CATEGORY_NOTIFY_LOTUS);
        intent1.setData(uri);
        notifyLotusIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);
        intent1.setAction(ACTION_SLOT_UNSATISFIED);
        notifyLotusUnsatisfiedIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SLOT_SATISFIED);
        filter.addCategory(CATEGORY_NOTIFY_LOTUS);
        filter.addDataScheme(uri.getScheme());
        filter.addDataAuthority(uri.getAuthority(), null);
        filter.addDataPath(uri.getPath(), PatternMatcher.PATTERN_LITERAL);
        context.registerReceiver(mReceiver, filter);

        mSlot = dataToSlot(eventTree.getEvent());
        mSlot.register(notifyLotusIntent, notifyLotusUnsatisfiedIntent);
    }

    private AbstractSlot dataToSlot(EventData data) {
        AbstractSlot slot;
        EventPlugin plugin = PluginRegistry.getInstance().findEventPlugin(data);
        slot = plugin.slot(context);
        slot.set(data);
        return slot;
    }

    void check() {
        mSlot.check();
    }

    void listen() {
        mSlot.listen();
    }

    void cancel() {
        mSlot.cancel();
        for (Lotus sub : subs) {
            sub.cancel();
        }
        subs.clear();
    }

    private synchronized void onSlotSatisfied() {
        Log.d(getClass().getSimpleName(), String.format("onSlotSatisfied %s", eventTree.getName()));
        if (!analyzing) {
            analyzing = true;
            if (mSlot.canPromoteSub()) {
                triggerAndPromote();
            } else {
                traverseAndTrigger(eventTree, false);
            }
            analyzing = false;
        }
    }

    private synchronized void onSlotUnsatisfied() {
        Log.d(getClass().getSimpleName(), String.format("onSlotUnsatisfied %s", eventTree.getName()));
        for (Lotus sub : subs) {
            sub.cancel();
        }
        subs.clear();
    }

    /*
     * 中序遍歷樹並尋找滿足條件（通過@AbstractSlot.isSatisfied()）的所有節點
     * 並在其處（所在的遞歸棧）載入對應Profile
     */
    private void traverseAndTrigger(EventTree node, boolean is_sub) {
        EventData eventData = node.getEvent();
        AbstractSlot slot = mSlot;
        if (is_sub) {
            slot = dataToSlot(eventData);
            slot.check();
        }
        if (slot.isSatisfied()) {
            Log.d(getClass().getSimpleName(),
                    String.format(" traversing and find %s satisfied", node.getName()));
            String profileName = node.getProfile();
            if (profileName != null) {
                Intent intent = new Intent(context, ProfileLoaderIntentService.class);
                intent.setAction(ProfileLoaderIntentService.ACTION_LOAD_PROFILE);
                intent.putExtra(ProfileLoaderIntentService.EXTRA_PROFILE_NAME, profileName);
                intent.putExtra(ProfileLoaderIntentService.EXTRA_EVENT_NAME, node.getName());
                context.startService(intent);
            }
            for (EventTree sub : node.getSubs()) {
                if (sub.isActive())
                    traverseAndTrigger(sub, true);
            }
        }
    }

    private void triggerAndPromote() {
        if (mSlot.isSatisfied()) {
            Log.d(getClass().getSimpleName(),
                    String.format(" traversing and find %s satisfied", eventTree.getName()));
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
                    Lotus subLotus = new Lotus(context, sub);
                    subs.add(subLotus);
                    subLotus.listen();
                    subLotus.check();
                }
            }
        }
    }
}
