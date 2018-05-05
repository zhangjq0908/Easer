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
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ryey.easer.R;
import ryey.easer.core.EHService;
import ryey.easer.core.EventHistoryRecord;

public class LoadedHistoryFragment extends Fragment {

    private static final String ARG_SIZE = "ryey.easer.core.ui.LoadedHistoryFragment.ARG.SIZE";
    private static final String COMPACT = "ryey.easer.core.ui.LoadedHistoryFragment.COMPACT";
    private static final String FULL = "ryey.easer.core.ui.LoadedHistoryFragment.FULL";

    static LoadedHistoryFragment compact() {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SIZE, COMPACT);
        LoadedHistoryFragment fragment = new LoadedHistoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    static LoadedHistoryFragment full() {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SIZE, FULL);
        LoadedHistoryFragment fragment = new LoadedHistoryFragment();
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
        if (args == null || FULL.equals(args.getString(ARG_SIZE))) {
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
            View view = inflater.inflate(R.layout.fragment_loaded_history, container, false);
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
            historyViewHolder.bindTo(EHService.getLastHistory());
        } else {
            historyAdapter.notifyDataSetChanged();
        }
    }

    public static class HistoryAdapter extends ListAdapter<EventHistoryRecord, HistoryViewHolder> {

        HistoryAdapter() {
            super(DIFF_CALLBACK);
            submitList(EHService.getHistory());
        }

        @NonNull
        @Override
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_loaded_history, parent, false);
            return new HistoryViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
            EventHistoryRecord historyRecord = getItem(position);
            holder.bindTo(historyRecord);
        }

        static final DiffUtil.ItemCallback<EventHistoryRecord> DIFF_CALLBACK =
                new DiffUtil.ItemCallback<EventHistoryRecord>() {
                    @Override
                    public boolean areItemsTheSame(
                            @NonNull EventHistoryRecord oldEventHistoryRecord, @NonNull EventHistoryRecord newEventHistoryRecord) {
                        return oldEventHistoryRecord.equals(newEventHistoryRecord);
                    }
                    @Override
                    public boolean areContentsTheSame(
                            @NonNull EventHistoryRecord oldEventHistoryRecord, @NonNull EventHistoryRecord newEventHistoryRecord) {
                        return oldEventHistoryRecord.equals(newEventHistoryRecord);
                    }
                };

    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_event, tv_profile, tv_time;
        HistoryViewHolder(View itemView) {
            super(itemView);
            tv_event = itemView.findViewById(R.id.textView_from_event);
            tv_profile = itemView.findViewById(R.id.textView_last_profile);
            tv_time = itemView.findViewById(R.id.textView_profile_load_time);
        }

        void bindTo(EventHistoryRecord historyRecord) {
            if (historyRecord == null)
                return;
            final String eventName = historyRecord.event;
            final String profileName = historyRecord.profile;
            long loadTime = historyRecord.loadTime;
            tv_profile.setText(profileName);
            tv_event.setText(eventName);
            if (loadTime > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(loadTime);
                DateFormat df = SimpleDateFormat.getDateTimeInstance();
                tv_time.setText(df.format(calendar.getTime()));
            } else {
                tv_time.setText("");
            }
        }
    }
}
