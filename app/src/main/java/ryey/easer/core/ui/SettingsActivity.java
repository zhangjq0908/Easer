package ryey.easer.core.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ryey.easer.R;
import ryey.easer.core.EHService;
import ryey.easer.core.data.Helper;

public class SettingsActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_pref_autostart))) {
            ComponentName componentName = new ComponentName(this, EHService.class);
            PackageManager pm = getPackageManager();
            if (sharedPreferences.getBoolean(key, false)) {
                pm.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            } else {
                pm.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }
    }

    public static class SettingsFragment extends PreferenceFragment {
        final int REQ_CODE = 10;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference pref_export = findPreference(getString(R.string.key_pref_export));
            pref_export.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.hh:mm:ss", Locale.US);
                        String time_str = sdf.format(calendar.getTime());
                        File sdCard = Environment.getExternalStorageDirectory();
                        File dest = new File(sdCard, String.format("Easer.%s.zip", time_str));
                        Logger.v("Export destination: %s", dest);
                        Helper.export_data(getActivity(), dest);
                        Toast.makeText(getActivity(),
                                String.format(getString(R.string.template_export), dest.getName()),
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });

            Preference pref_import = findPreference(getString(R.string.key_pref_import));
            pref_import.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("file/*");
                    startActivityForResult(intent, REQ_CODE);
                    return true;
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQ_CODE) {
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        Helper.import_data(getActivity(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    EHService.reload(getActivity());
                }
            }
        }
    }
}
