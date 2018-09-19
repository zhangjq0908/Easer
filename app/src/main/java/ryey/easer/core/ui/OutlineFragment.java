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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ryey.easer.R;
import ryey.easer.core.EHService;

public class OutlineFragment extends Fragment {
    View mView;

    TextView mIndicator;
    ImageView mBanner;

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case EHService.ACTION_STATE_CHANGED:
                    refresh();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_outline));
        mView = inflater.inflate(R.layout.fragment_outline, container, false);

        mIndicator = mView.findViewById(R.id.running_ind);
        mBanner = mView.findViewById(R.id.running_ind_banner);

        Fragment fragment_history = ActivityHistoryFragment.compact();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content_fragment_loaded_history, fragment_history)
                .commit();

        mView.findViewById(R.id.holder_running_ind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!EHService.isRunning()) {
                    EHService.start(getContext());
                }
            }
        });
        mView.findViewById(R.id.holder_running_ind).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (EHService.isRunning()) {
                    EHService.stop(getContext());
                    return true;
                }
                return false;
            }
        });

        mView.findViewById(R.id.content_fragment_loaded_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_log);
                //noinspection ConstantConditions
                ((MainActivity) getContext()).changeUIView(R.id.nav_log);
            }
        });

        FloatingActionButton fab = mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EHService.reload(getActivity());
            }
        });

        IntentFilter filter = new IntentFilter(EHService.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mReceiver, filter);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.outline, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_start) {
            EHService.start(getActivity());
            return true;
        }
        if (id == R.id.action_stop) {
            EHService.stop(getActivity());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        int color;
        if (EHService.isRunning()) {
            mIndicator.setText(getResources().getString(R.string.service_indicator_positive));
            mBanner.setImageResource(R.drawable.ic_status_positive);
            color = getResources().getColor(R.color.color_positive);
        } else {
            mIndicator.setText(getResources().getString(R.string.service_indicator_negative));
            mBanner.setImageResource(R.drawable.ic_status_negative);
            color = getResources().getColor(R.color.color_negative);
        }
        mIndicator.setTextColor(color);
        mBanner.setBackgroundColor(color);
    }

}
