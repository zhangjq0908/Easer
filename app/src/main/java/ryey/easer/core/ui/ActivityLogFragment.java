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

package ryey.easer.core.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ryey.easer.R;
import ryey.easer.core.EHService;
import ryey.easer.core.log.ActivityLog;
import ryey.easer.core.log.ActivityLogService;
import ryey.easer.core.log.ProfileLoadedLog;
import ryey.easer.core.log.ScriptSatisfactionLog;
import ryey.easer.core.log.ServiceLog;

public class ActivityLogFragment extends Fragment {

    private static final String ARG_SIZE = "ryey.easer.core.ui.ActivityLogFragment.ARG.SIZE";
    private static final int COMPACT = 1;
    private static final int FULL = 0;

    static ActivityLogFragment compact() {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SIZE, COMPACT);
        ActivityLogFragment fragment = new ActivityLogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    static ActivityLogFragment full() {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SIZE, FULL);
        ActivityLogFragment fragment = new ActivityLogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case EHService.ACTION_PROFILE_UPDATED:
                    refreshHistoryDisplay();
                    break;
            }
        }
    };

    HistoryViewHolder historyViewHolder;
    HistoryAdapter historyAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null || FULL == args.getInt(ARG_SIZE)) {
            getActivity().setTitle(R.string.title_activity_log);
            View layout = inflater.inflate(R.layout.fragment_loaded_history_full, container, false);
            RecyclerView view = layout.findViewById(R.id.recycler_view);
            view.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            view.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(),
                    layoutManager.getOrientation());
            view.addItemDecoration(dividerItemDecoration);
            historyAdapter = new HistoryAdapter();
            view.setAdapter(historyAdapter);
            return layout;
        } else {
            View view = inflater.inflate(R.layout.item_activity_log, container, false);
            historyViewHolder = new HistoryViewHolder(view);
            return view;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(EHService.ACTION_PROFILE_UPDATED);
        getContext().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshHistoryDisplay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }

    private void refreshHistoryDisplay() {
        if (historyViewHolder != null) {
            historyViewHolder.bindTo(ActivityLogService.Companion.getLastHistory());
        } else {
            historyAdapter.notifyDataSetChanged();
        }
    }

    public static class HistoryAdapter extends ListAdapter<ActivityLog, HistoryViewHolder> {

        HistoryAdapter() {
            super(DIFF_CALLBACK);
            submitList(ActivityLogService.Companion.getHistory());
        }

        @NonNull
        @Override
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_activity_log, parent, false);
            return new HistoryViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
            ActivityLog log = getItem(position);
            holder.bindTo(log);
        }

        static final DiffUtil.ItemCallback<ActivityLog> DIFF_CALLBACK =
                new DiffUtil.ItemCallback<ActivityLog>() {
                    @Override
                    public boolean areItemsTheSame(
                            @NonNull ActivityLog oldActivityLog, @NonNull ActivityLog newActivityLog) {
                        return oldActivityLog.equals(newActivityLog);
                    }
                    @Override
                    public boolean areContentsTheSame(
                            @NonNull ActivityLog oldActivityLog, @NonNull ActivityLog newActivityLog) {
                        return oldActivityLog.equals(newActivityLog);
                    }
                };

    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        final TableRow c_time, c_extra, c_script, c_satisfaction, c_profile, c_service, c_status;
        final TextView tv_time, tv_extra, tv_script, tv_satisfaction, tv_profile, tv_service, tv_status;
        HistoryViewHolder(View itemView) {
            super(itemView);
            c_time = itemView.findViewById(R.id.c_time);
            tv_time = itemView.findViewById(R.id.textView_profile_load_time);
            c_script = itemView.findViewById(R.id.c_script);
            tv_script = itemView.findViewById(R.id.textView_from_event);
            c_satisfaction = itemView.findViewById(R.id.c_script_satisfaction);
            tv_satisfaction = itemView.findViewById(R.id.textView_extra_satisfaction);
            c_profile = itemView.findViewById(R.id.c_profile);
            tv_profile = itemView.findViewById(R.id.textView_last_profile);
            c_extra = itemView.findViewById(R.id.c_extra);
            tv_extra = itemView.findViewById(R.id.textView_extra_info);
            c_service = itemView.findViewById(R.id.c_service);
            tv_service = itemView.findViewById(R.id.textView_service);
            c_status = itemView.findViewById(R.id.c_status);
            tv_status = itemView.findViewById(R.id.textView_status);
        }

        @Nullable
        private static String tLong2Text(long time) {
            if (time < 0) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            DateFormat df = SimpleDateFormat.getDateTimeInstance();
            return df.format(calendar.getTime());
        }

        void bindTo(@Nullable ActivityLog activityLog) {
            c_script.setVisibility(View.GONE);
            c_profile.setVisibility(View.GONE);
            c_time.setVisibility(View.GONE);
            c_extra.setVisibility(View.GONE);
            c_satisfaction.setVisibility(View.GONE);
            c_service.setVisibility(View.GONE);
            c_status.setVisibility(View.GONE);
            if (activityLog == null) {
                return;
            }
            long loadTime = activityLog.time();
            c_time.setVisibility(View.VISIBLE);
            tv_time.setText(tLong2Text(loadTime));
            String extraInfo = activityLog.extraInfo();
            if (extraInfo != null) {
                c_extra.setVisibility(View.VISIBLE);
                tv_extra.setText(extraInfo);
            }
            if (activityLog instanceof ScriptSatisfactionLog) {
                ScriptSatisfactionLog log = (ScriptSatisfactionLog) activityLog;
                final String scriptName = (log).getScriptName();
                c_script.setVisibility(View.VISIBLE);
                tv_script.setText(scriptName);
                c_satisfaction.setVisibility(View.VISIBLE);
                if (log.getSatisfaction()) {
                    final String profileName = (log).getProfileName();
                    if (profileName != null) {
                        c_profile.setVisibility(View.VISIBLE);
                        tv_profile.setText(profileName);
                    }
                    tv_satisfaction.setText(R.string.activity_log__satisfied);
                } else {
                    tv_satisfaction.setText(R.string.activity_log__unsatisfied);
                }
            } else {
                if (activityLog instanceof ProfileLoadedLog) {
                    ProfileLoadedLog log = (ProfileLoadedLog) activityLog;
                    final String profileName = (log).getProfileName();
                    c_profile.setVisibility(View.VISIBLE);
                    tv_profile.setText(profileName);
                } else if (activityLog instanceof ServiceLog) {
                    ServiceLog log = (ServiceLog) activityLog;
                    final String serviceName = log.getServiceName();
                    final boolean start = log.getStart();
                    c_service.setVisibility(View.VISIBLE);
                    tv_service.setText(serviceName);
                    c_status.setVisibility(View.VISIBLE);
                    tv_status.setText(start ? R.string.activity_log__start : R.string.activity_log__stop);
                }
            }
        }
    }
}
