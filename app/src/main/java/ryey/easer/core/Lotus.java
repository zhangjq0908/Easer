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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.PatternMatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.orhanobut.logger.Logger;

import java.util.Locale;

import ryey.easer.core.data.LogicGraph;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.log.ActivityLogService;
import ryey.easer.skills.operation.state_control.StateControlOperationSkill;

/**
 * Each Lotus holds one {@link ryey.easer.core.data.LogicGraph.LogicNode}.
 */
public abstract class Lotus {
    public static final String ACTION_LOTUS_SATISFACTION_CHANGED = "ryey.easer.lotus.action.LOTUS_SATISFACTION_CHANGED";
    public static final String EXTRA_SATISFACTION = "ryey.easer.lotus.extra.LOTUS_SATISFACTION";
    public static final String EXTRA_SCRIPT_ID = "ryey.easer.lotus.extra.SCRIPT_ID";

    private static final String ACTION_SLOT_SATISFIED = "ryey.easer.triggerlotus.action.SLOT_SATISFIED";
    private static final String ACTION_SLOT_UNSATISFIED = "ryey.easer.triggerlotus.action.SLOT_UNSATISFIED";
    private static final String CATEGORY_NOTIFY_LOTUS = "ryey.easer.triggerlotus.category.NOTIFY_LOTUS";

    public static final String EXTRA_DYNAMICS_PROPERTIES = "ryey.easer.core.lotus.extras.DYNAMICS_PROPERTIES";
    static final String EXTRA_DYNAMICS_LINK = "ryey.easer.core.lotus.extras.DYNAMICS_LINK";

    static Lotus createLotus(@NonNull Context context, @NonNull LogicGraph.LogicNode node,
                             @NonNull CoreServiceComponents.LogicManager logicManager,
                             @NonNull CoreServiceComponents.DelayedConditionHolderBinderJobs jobCH,
                             @NonNull AsyncHelper.DelayedLoadProfileJobs jobLP) {
        if (node.script.isEvent())
            return new EventLotus(context, node, logicManager, jobLP);
        else
            return new ConditionLotus(context, node, logicManager, jobCH, jobLP);
    }

    @NonNull protected final Context context;
    @NonNull protected final LogicGraph.LogicNode node;
    @NonNull protected final CoreServiceComponents.LogicManager logicManager;
    @NonNull protected final AsyncHelper.DelayedLoadProfileJobs jobLP;

    protected boolean satisfied = false;

    protected final Uri uri = Uri.parse(String.format(Locale.US, "lotus://%d", hashCode()));

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION_SLOT_SATISFIED.equals(action) || ACTION_SLOT_UNSATISFIED.equals(action)) {
                onStateSignal(ACTION_SLOT_SATISFIED.equals(action), intent.getExtras());
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

    protected Lotus(@NonNull Context context, @NonNull LogicGraph.LogicNode node,
                    @NonNull CoreServiceComponents.LogicManager logicManager,
                    @NonNull AsyncHelper.DelayedLoadProfileJobs jobLP) {
        this.context = context;
        this.node = node;
        this.logicManager = logicManager;
        this.jobLP = jobLP;
    }

    /**
     * TODO: merge with {@link #node}
     */
    protected ScriptStructure script() {
        return node.script;
    }

    final synchronized void listen() {
        context.registerReceiver(mReceiver, filter);
        onListen();
    }
    protected void onListen() {
    }

    final synchronized void cancel() {
        context.unregisterReceiver(mReceiver);
        onCancel();
        logicManager.deactivateSuccessors(node);
    }
    protected void onCancel() {
    }

    /**
     * Dirty hack for {@link StateControlOperationSkill}
     * TODO: cleaner solution
     * @param status new status for the top level slot of this lotus
     */
    synchronized void setStatus(boolean status) {
        if (status) {
            onSatisfied(null);
        } else {
            onUnsatisfied();
        }
    }

    protected void sendSatisfactionChangeBroadcast(boolean satisfied) {
        Intent intent = new Intent(ACTION_LOTUS_SATISFACTION_CHANGED);
        intent.putExtra(EXTRA_SATISFACTION, satisfied);
        intent.putExtra(EXTRA_SCRIPT_ID, script().getName());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    protected void onStateSignal(boolean state) {
        onStateSignal(state, null);
    }

    /**
     * This method is called whenever there is a signal for this Lotus.
     * Caller should take care not to call this unexpectedly (e.g. not call this multiple times for the same state unless really want).
     * @param state The "current"/"new" state of the condition/event.
     * @param extras Potential extra information for the condition/event.
     */
    protected void onStateSignal(boolean state, @Nullable Bundle extras) {
        if (state != script().isReverse()) {
            onSatisfied(extras);
        } else {
            onUnsatisfied();
        }
    }

    protected void onSatisfied(@Nullable Bundle extras) {
        Logger.i("Lotus for <%s> satisfied", script().getName());
        satisfied = true;
        sendSatisfactionChangeBroadcast(true);

        String profileName = script().getProfileName();
        if (profileName != null) {
            if (extras == null)
                extras = new Bundle();
            jobLP.triggerProfile(profileName, script().getName(),
                    extras, script().getDynamicsLink());
        }

        logicManager.activateSuccessors(node);
    }

    protected void onUnsatisfied() {
        Logger.i("Lotus for <%s> unsatisfied", script().getName());
        satisfied = false;
        sendSatisfactionChangeBroadcast(false);

        ActivityLogService.Companion.notifyScriptUnsatisfied(context, script().getName(), null);

        logicManager.deactivateSuccessors(node);
    }

    protected boolean status() {
        return satisfied;
    }

    public static class NotifyIntentPrototype {
        //TODO: Extract interface to ryey.easer.commons

        public static Intent obtainPositiveIntent(Uri data) {
            return obtainPositiveIntent(data, null);
        }

        public static Intent obtainPositiveIntent(Uri data, @Nullable Bundle dynamics) {
            Intent intent = new Intent(ACTION_SLOT_SATISFIED);
            intent.addCategory(CATEGORY_NOTIFY_LOTUS);
            intent.setData(data);
            intent.putExtra(Lotus.EXTRA_DYNAMICS_PROPERTIES, dynamics);
            return intent;
        }

        public static Intent obtainNegativeIntent(Uri data) {
            return obtainNegativeIntent(data, null);
        }

        public static Intent obtainNegativeIntent(Uri data, @Nullable Bundle dynamics) {
            Intent intent = new Intent(ACTION_SLOT_UNSATISFIED);
            intent.addCategory(CATEGORY_NOTIFY_LOTUS);
            intent.setData(data);
            intent.putExtra(Lotus.EXTRA_DYNAMICS_PROPERTIES, dynamics);
            return intent;
        }
    }
}
