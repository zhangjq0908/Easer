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

package ryey.easer.core.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Calendar;

import ryey.easer.R;
import ryey.easer.SettingsHelper;
import ryey.easer.Utils;
import ryey.easer.core.EHService;
import ryey.easer.core.log.ActivityLog;
import ryey.easer.core.log.ActivityLogService;
import ryey.easer.core.log.ProfileLoadedLog;
import ryey.easer.core.log.ScriptSatisfactionLog;
import ryey.easer.core.log.ServiceLog;
import ryey.easer.databinding.ItemActivityLogBinding;

public class ActivityHistoryFragment extends Fragment {

    private static final String ARG_SIZE = "ryey.easer.core.ui.ActivityHistoryFragment.ARG.SIZE";
    private static final int COMPACT = 1;
    private static final int FULL = 0;

    static ActivityHistoryFragment compact() {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SIZE, COMPACT);
        ActivityHistoryFragment fragment = new ActivityHistoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    static ActivityHistoryFragment full() {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SIZE, FULL);
        ActivityHistoryFragment fragment = new ActivityHistoryFragment();
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

    private int size = FULL;
    HistoryViewHolder historyViewHolder;
    HistoryAdapter historyAdapter;

    boolean clearHistoryAfterBind = false;
    ActivityLogService.ActivityLogServiceBinder serviceBinder;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceBinder = (ActivityLogService.ActivityLogServiceBinder) service;
            if (clearHistoryAfterBind) {
                clearHistory();
                clearHistoryAfterBind = false;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBinder = null;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null && COMPACT == args.getInt(ARG_SIZE))
            size = COMPACT;
        if (size == FULL) {
            getActivity().setTitle(R.string.title_activity_history);
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
            setHasOptionsMenu(true);
            return layout;
        } else {
            View view = inflater.inflate(R.layout.item_activity_log, container, false);
            historyViewHolder = new HistoryViewHolder(view, new WeakReference<>(getContext()));
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
        getContext().bindService(new Intent(getContext(), ActivityLogService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unbindService(serviceConnection);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear) {
            if (serviceBinder == null)
                clearHistoryAfterBind = true;
            else
                clearHistory();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void clearHistory() {
        serviceBinder.clearLog();
        historyAdapter.notifyDataSetChanged();
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
            return new HistoryViewHolder(v, new WeakReference<>(parent.getContext()));
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
        final ItemActivityLogBinding binding;
        final WeakReference<Context> context;
        HistoryViewHolder(View itemView, WeakReference<Context> context) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            this.context = context;
        }

        @Nullable
        private static String tLong2Text(long time, Context context) {
            if (time < 0) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            DateFormat df;
            if (SettingsHelper.use12HourClock(context)) {
                df = Utils.df_12hour;
            } else {
                df = Utils.df_24hour;
            }
            return df.format(calendar.getTime());
        }

        void bindTo(@Nullable ActivityLog activityLog) {
            binding.cScript.setVisibility(View.GONE);
            binding.cStatus.setVisibility(View.GONE);
            binding.cProfile.setVisibility(View.GONE);
            binding.cService.setVisibility(View.GONE);
            binding.cTime.setVisibility(View.GONE);
            binding.cExtra.setVisibility(View.GONE);
            if (activityLog == null)
                return;
            long loadTime = activityLog.time();
            binding.cTime.setVisibility(View.VISIBLE);
            binding.tvTime.setText(tLong2Text(loadTime, context.get()));
            String extraInfo = activityLog.extraInfo();
            if (extraInfo != null) {
                binding.cExtra.setVisibility(View.VISIBLE);
                binding.tvExtra.setText(extraInfo);
            }
            if (activityLog instanceof ScriptSatisfactionLog) {
                ScriptSatisfactionLog log = (ScriptSatisfactionLog) activityLog;
                final String scriptName = (log).getScriptName();
                binding.cScript.setVisibility(View.VISIBLE);
                binding.tvScript.setText(scriptName);
                final String profileName = (log).getProfileName();
                if (profileName != null) {
                    binding.cProfile.setVisibility(View.VISIBLE);
                    binding.tvProfile.setText(profileName);
                }
                binding.cStatus.setVisibility(View.VISIBLE);
                binding.tvStatus.setText(log.getSatisfaction()
                        ? R.string.activity_history__satisfied
                        : R.string.activity_history__unsatisfied);
            } else {
                if (activityLog instanceof ProfileLoadedLog) {
                    ProfileLoadedLog log = (ProfileLoadedLog) activityLog;
                    final String profileName = (log).getProfileName();
                    binding.cProfile.setVisibility(View.VISIBLE);
                    binding.tvProfile.setText(profileName);
                } else if (activityLog instanceof ServiceLog) {
                    ServiceLog log = (ServiceLog) activityLog;
                    final String serviceName = log.getServiceName();
                    final boolean start = log.getStart();
                    binding.cService.setVisibility(View.VISIBLE);
                    binding.tvService.setText(serviceName);
                    binding.cStatus.setVisibility(View.VISIBLE);
                    binding.tvStatus.setText(start
                            ? R.string.activity_history__start
                            : R.string.activity_history__stop);
                }
            }
        }
    }
}
