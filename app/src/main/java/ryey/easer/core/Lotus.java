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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

import ryey.easer.core.data.ScriptTree;

/**
 * Each Lotus holds one ScriptTree.
 */
abstract class Lotus {
    private static final String ACTION_SLOT_SATISFIED = "ryey.easer.triggerlotus.action.SLOT_SATISFIED";
    private static final String ACTION_SLOT_UNSATISFIED = "ryey.easer.triggerlotus.action.SLOT_UNSATISFIED";
    private static final String CATEGORY_NOTIFY_LOTUS = "ryey.easer.triggerlotus.category.NOTIFY_LOTUS";

    static Lotus createLotus(@NonNull Context context, @NonNull ScriptTree scriptTree,
                             @NonNull ExecutorService executorService,
                             @NonNull ConditionHolderService.CHBinder chBinder) {
        if (scriptTree.isEvent())
            return new EventLotus(context, scriptTree, executorService, chBinder);
        else
            return new ConditionLotus(context, scriptTree, executorService, chBinder);
    }

    @NonNull protected final Context context;
    @NonNull protected final ScriptTree scriptTree;
    @NonNull protected final ExecutorService executorService;
    @NonNull protected final ConditionHolderService.CHBinder chBinder;
    protected List<Lotus> subs = new ArrayList<>();

    protected boolean satisfied = false;

    private final Uri uri = Uri.parse(String.format(Locale.US, "lotus://%d", hashCode()));
    protected final NotifyPendingIntents notifyPendingIntents;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION_SLOT_SATISFIED.equals(action) || ACTION_SLOT_UNSATISFIED.equals(action)) {
                onStateSignal(ACTION_SLOT_SATISFIED.equals(action));
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

    protected Lotus(@NonNull Context context, @NonNull ScriptTree scriptTree, @NonNull ExecutorService executorService, @NonNull ConditionHolderService.CHBinder chBinder) {
        this.context = context;
        this.scriptTree = scriptTree;
        this.executorService = executorService;
        this.chBinder = chBinder;

        Intent intent = new Intent(ACTION_SLOT_SATISFIED);
        intent.addCategory(CATEGORY_NOTIFY_LOTUS);
        intent.setData(uri);
        PendingIntent notifyLotusIntent_positive = PendingIntent.getBroadcast(context, 0, intent, 0);
        intent.setAction(ACTION_SLOT_UNSATISFIED);
        PendingIntent notifyLotusIntent_negative = PendingIntent.getBroadcast(context, 0, intent, 0);
        notifyPendingIntents = new NotifyPendingIntents(notifyLotusIntent_positive, notifyLotusIntent_negative);
    }

    final @NonNull String scriptName() {
        return scriptTree.getName();
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
        for (Lotus sub : subs) {
            sub.cancel();
        }
        subs.clear();
    }
    protected void onCancel() {
    }

    /**
     * Dirty hack for {@link ryey.easer.plugins.operation.state_control.StateControlOperationPlugin}
     * TODO: cleaner solution
     * @param status new status for the top level slot of this lotus
     */
    synchronized void setStatus(boolean status) {
        if (status) {
            onSatisfied();
        } else {
            onUnsatisfied();
        }
    }

    protected void onStateSignal(boolean state) {
        if (state != scriptTree.isReversed()) {
            onSatisfied();
        } else {
            onUnsatisfied();
        }
    }

    protected void onSatisfied() {
        Logger.i("Lotus for <%s> satisfied", scriptTree.getName());
        satisfied = true;
        triggerAndPromote();
    }

    protected void onUnsatisfied() {
        Logger.i("Lotus for <%s> unsatisfied", scriptTree.getName());
        satisfied = false;
        for (Lotus sub : subs) {
            sub.cancel();
        }
        subs.clear();
    }

    synchronized protected void triggerAndPromote() {
        Logger.v(" traversing and find <%s> satisfied", scriptTree.getName());
        String profileName = scriptTree.getProfile();
        if (profileName != null) {
            Intent intent = new Intent(context, ProfileLoaderIntentService.class);
            intent.setAction(ProfileLoaderIntentService.ACTION_LOAD_PROFILE);
            intent.putExtra(ProfileLoaderIntentService.EXTRA_PROFILE_NAME, profileName);
            intent.putExtra(ProfileLoaderIntentService.EXTRA_EVENT_NAME, scriptTree.getName());
            context.startService(intent);
        }
        for (ScriptTree sub : scriptTree.getSubs()) {
            if (sub.isActive()) {
                Lotus subLotus = Lotus.createLotus(context, sub, executorService, chBinder);
                subs.add(subLotus);
                subLotus.listen();
            }
        }
    }

    static class NotifyPendingIntents {
        final PendingIntent positive, negative;

        public NotifyPendingIntents(PendingIntent positive, PendingIntent negative) {
            this.positive = positive;
            this.negative = negative;
        }
    }
}
