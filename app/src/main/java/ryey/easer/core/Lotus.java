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
 * Currently, Lotus uses *post* order traversal to find the *first* satisfied child with Profile.
 */
final class Lotus {
    private static final String ACTION_SLOT_SATISFIED = "ryey.easer.triggerlotus.action.SLOT_SATISFIED";
    private static final String CATEGORY_NOTIFY_LOTUS = "ryey.easer.triggerlotus.category.NOTIFY_LOTUS";

    private Context context;
    private final EventTree eventTree;

    private final Uri uri = Uri.parse(String.format("lotus://%d", hashCode()));
    private final PendingIntent notifyLotusIntent;

    private AbstractSlot mSlot;

    private boolean analyzing = false;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SLOT_SATISFIED))
                onSlotSatisfied();
        }
    };

    Lotus(Context context, EventTree eventTree) {
        this.context = context;
        this.eventTree = eventTree;

        Intent intent1 = new Intent(ACTION_SLOT_SATISFIED);
        intent1.addCategory(CATEGORY_NOTIFY_LOTUS);
        intent1.setData(uri);
        notifyLotusIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SLOT_SATISFIED);
        filter.addCategory(CATEGORY_NOTIFY_LOTUS);
        filter.addDataScheme(uri.getScheme());
        filter.addDataAuthority(uri.getAuthority(), null);
        filter.addDataPath(uri.getPath(), PatternMatcher.PATTERN_LITERAL);
        context.registerReceiver(mReceiver, filter);

        mSlot = dataToSlot(eventTree.getEvent());
        mSlot.register(notifyLotusIntent);
    }

    private AbstractSlot dataToSlot(EventData data) {
        AbstractSlot slot;
        for (EventPlugin plugin : PluginRegistry.getInstance().getEventPlugins()) {
            if (data.pluginClass() == plugin.getClass()) {
                slot = plugin.slot(context);
                slot.set(data);
                return slot;
            }
        }
        throw new IllegalAccessError();
    }

    void apply() {
        mSlot.apply();
    }

    void cancel() {
        mSlot.cancel();
    }

    private synchronized void onSlotSatisfied() {
        Log.d(getClass().getSimpleName(), String.format("onSlotSatisfied %s", eventTree.getName()));
        if (!analyzing) {
            analyzing = true;
            findLastAndTrigger(eventTree, false);
            analyzing = false;
        }
    }

    /*
     * DFS尋找第一個滿足所有條件（通過@AbstractSlot.isSatisfied()）的節點
     * 並在其處（所在的遞歸棧）載入對應Profile
     */
    private boolean findLastAndTrigger(EventTree node, boolean is_sub) {
        EventData eventData = node.getEvent();
        AbstractSlot slot = mSlot;
        if (is_sub) {
            slot = dataToSlot(eventData);
            slot.check();
        }
        if (slot.isSatisfied()) {
            boolean handled_by_sub = false;
            for (EventTree sub : node.getSubs()) {
                handled_by_sub = handled_by_sub || findLastAndTrigger(sub, true);
            }
            if (!handled_by_sub) {
                Log.d(getClass().getSimpleName(), " got last satisfied");
                String profileName = node.getProfile();
                if (profileName != null) {
                    Intent intent = new Intent(context, ProfileLoaderIntentService.class);
                    intent.setAction(ProfileLoaderIntentService.ACTION_LOAD_PROFILE);
                    intent.putExtra(ProfileLoaderIntentService.EXTRA_PROFILE_NAME, profileName);
                    context.startService(intent);
                    return true;
                } else
                    return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
