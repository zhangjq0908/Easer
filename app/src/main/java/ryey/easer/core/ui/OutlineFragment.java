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

package ryey.easer.core.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ryey.easer.R;
import ryey.easer.core.EHService;

public class OutlineFragment extends Fragment {
    View mView;

    TextView mIndicator;
    ImageView mBanner;

    TextView mLastProfile, mFromEvent, mTimeLoaded;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case EHService.ACTION_STATE_CHANGED:
                    refresh();
                    break;
                case EHService.ACTION_PROFILE_UPDATED:
                    updateProfileDisplay();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(getString(R.string.title_outline));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_outline, container, false);

        mIndicator = (TextView) mView.findViewById(R.id.running_ind);
        mBanner = (ImageView) mView.findViewById(R.id.running_ind_banner);

        mLastProfile = (TextView) mView.findViewById(R.id.textView_last_profile);
        mFromEvent = (TextView) mView.findViewById(R.id.textView_from_event);
        mTimeLoaded = (TextView) mView.findViewById(R.id.textView_profile_load_time);

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EHService.reload(getActivity());
            }
        });

        IntentFilter filter = new IntentFilter(EHService.ACTION_STATE_CHANGED);
        filter.addAction(EHService.ACTION_PROFILE_UPDATED);
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

        if (id == R.id.action_about) {
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
            return true;
        }

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
        updateProfileDisplay();
    }

    private void updateProfileDisplay() {
        final String profileName = EHService.getLastProfile();
        final String eventName = EHService.getFromEvent();
        long loadTime = EHService.getLoadTime();
        mLastProfile.setText(profileName);
        mFromEvent.setText(eventName);
        if (loadTime > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(loadTime);
            DateFormat df = SimpleDateFormat.getDateTimeInstance();
            mTimeLoaded.setText(df.format(calendar.getTime()));
        } else {
            mTimeLoaded.setText("");
        }
    }
}
