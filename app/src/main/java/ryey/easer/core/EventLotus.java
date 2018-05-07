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

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;

import ryey.easer.SettingsHelper;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.ScriptTree;
import ryey.easer.plugins.PluginRegistry;

/*
 * Note: old document; may be outdated.
 *
 * The root of that tree will be registered listener.
 * When the root is satisfied, Lotus will recursively check all children to find which is satisfied.
 * After finding a satisfied child and the child has a Profile, Lotus will load the Profile.
 *
 * See onSlotSatisfied() for the actual way to check and determine which is satisfied.
 */
class EventLotus extends Lotus {

    private AbstractSlot mSlot;

    private final long cooldownInMillisecond;
    private Calendar lastSatisfied;

    private final boolean repeatable;
    private final boolean persistent;

    EventLotus(Context context, ScriptTree scriptTree, ExecutorService executorService, EHService.ConditionHolder conditionHolder) {
        super(context, scriptTree, executorService, conditionHolder);

        mSlot = nodeToSlot(scriptTree);
        if (scriptTree.isReversed()) {
            mSlot.register(notifyPendingIntents.negative, notifyPendingIntents.positive);
        } else {
            mSlot.register(notifyPendingIntents.positive, notifyPendingIntents.negative);
        }
        repeatable = scriptTree.isRepeatable();
        persistent = scriptTree.isPersistent();

        cooldownInMillisecond = SettingsHelper.coolDownInterval(context);
    }

    private <T extends EventData> AbstractSlot<T> nodeToSlot(ScriptTree node) {
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

    private synchronized void check() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mSlot.check();
            }
        });
    }

    protected synchronized void onListen() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mSlot.listen();
                if (!SettingsHelper.passiveMode(context)) {
                    check();
                }
            }
        });
    }

    protected synchronized void onCancel() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mSlot.cancel();
            }
        });
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

    protected synchronized void onSatisfied() {
        if (!repeatable && satisfied)
            return;
        if (checkAndSetCooldown(scriptTree.getName())) {
            super.onSatisfied();
        }
    }

    protected synchronized void onUnsatisfied() {
        if (persistent && satisfied)
            return;
        if (checkAndSetCooldown(scriptTree.getName())) {
            super.onUnsatisfied();
        }
    }

}
