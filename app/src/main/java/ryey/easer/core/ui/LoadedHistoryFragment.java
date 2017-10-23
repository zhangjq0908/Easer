package ryey.easer.core.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loaded_history, container, false);

        mLastProfile = (TextView) view.findViewById(R.id.textView_last_profile);
        mFromEvent = (TextView) view.findViewById(R.id.textView_from_event);
        mTimeLoaded = (TextView) view.findViewById(R.id.textView_profile_load_time);

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
