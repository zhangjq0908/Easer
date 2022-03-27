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

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.orhanobut.logger.Logger;

import ryey.easer.core.data.LogicGraph;
import ryey.easer.skills.event.widget.UserActionWidget;

final class CoreServiceComponents {
    private CoreServiceComponents() {}


    public interface LogicManager {

        /**
         * Attempt to activate the specified node.
         * It should not be activated multiple times.
         * @param node The node to activate.
         * @param from The ID (of a node) from which to activate the specified node.
         */
        void activateNode(LogicGraph.LogicNode node, String from);

        /**
         * Attempt to deactivate the specified node.
         * It should not be deactivated if still being used.
         * @param node The node to deactivate.
         * @param from The ID (of a node) from which to deactivate the specified node.
         */
        void deactivateNode(LogicGraph.LogicNode node, String from);

        /**
         * Activate the successors of the specified node.
         * If a successor is activated already, it should not be activated for multiple times.
         * @param node The node whose successors are to be be activated.
         */
        void activateSuccessors(LogicGraph.LogicNode node);

        /**
         * Deactivate the successors of the specified node.
         * If a successor is also activated by other nodes, it should not be deactivated.
         * @param node The node whose successors are to be deactivated.
         */
        void deactivateSuccessors(LogicGraph.LogicNode node);
    }

    static class CoreSkillHelper {
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
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
        public static void unregisterConditionEventNotifier(@NonNull Context context, @NonNull String conditionName, @NonNull Uri notifyData) {
            Intent intent = new Intent(ACTION_UNREGISTER_CONDITION_EVENT);
            intent.putExtra(EXTRA_CONDITION_NAME, conditionName);
            intent.putExtra(EXTRA_NOTIFY_DATA, notifyData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        /**
         * For {@link ryey.easer.skills.event.condition_event.ConditionEventEventSkill}
         */
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Logger.d("Broadcast received :: action: <%s>", action);
                if (ACTION_REGISTER_CONDITION_EVENT.equals(intent.getAction()) || ACTION_UNREGISTER_CONDITION_EVENT.equals(intent.getAction())) {
                    String conditionName = intent.getStringExtra(EXTRA_CONDITION_NAME);
                    Uri notifyData = intent.getParcelableExtra(EXTRA_NOTIFY_DATA);
                    if (ACTION_REGISTER_CONDITION_EVENT.equals(intent.getAction()))
                        jobCH.doAfter(binder -> binder.registerAssociation(conditionName, notifyData));
                    else
                        jobCH.doAfter(binder -> binder.unregisterAssociation(conditionName, notifyData));
                }
            }
        };

        private final Context context;
        private final DelayedConditionHolderBinderJobs jobCH;

        CoreSkillHelper(Context context, DelayedConditionHolderBinderJobs jobCH) {
            this.context = context;
            this.jobCH = jobCH;
        }

        /**
         * For {@link ryey.easer.skills.event.widget.WidgetEventSkill}
         */
        WidgetBroadcastRedispatcher widgetBroadcastRedispatcher = new WidgetBroadcastRedispatcher();

        void onCreate() {
            widgetBroadcastRedispatcher.start(context);
            LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, filter_conditionEvent);
        }

        void onDestroy() {
            widgetBroadcastRedispatcher.stop(context);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiver);
        }

        /**
         * For {@link ryey.easer.skills.event.widget.WidgetEventSkill}
         */
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

    static class DelayedConditionHolderBinderJobs extends AsyncHelper.DelayedServiceBinderJobs<ConditionHolderService.CHBinder> {

    }
}
