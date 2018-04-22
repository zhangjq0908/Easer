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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ryey.easer.R;
import ryey.easer.core.EHService;

public class LoadedHistoryFragment extends Fragment {

    TextView mLastProfile, mFromEvent, mTimeLoaded;

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case EHService.ACTION_PROFILE_UPDATED:
                    updateProfileDisplay();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loaded_history, container, false);

        mLastProfile = view.findViewById(R.id.textView_last_profile);
        mFromEvent = view.findViewById(R.id.textView_from_event);
        mTimeLoaded = view.findViewById(R.id.textView_profile_load_time);

        IntentFilter filter = new IntentFilter();
        filter.addAction(EHService.ACTION_PROFILE_UPDATED);
        getContext().registerReceiver(mReceiver, filter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProfileDisplay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
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
