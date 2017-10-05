package ryey.easer.core.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.locks.Lock;

import ryey.easer.R;
import ryey.easer.core.EHService;
import ryey.easer.core.data.Helper;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

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
        final static int REQCODE_PERM_STORAGE = 1;

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

            Preference pref_logging = findPreference(getString(R.string.key_pref_logging));
            pref_logging.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if ((Boolean) newValue == true) {
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            Logger.i("Permission <%s> not granted. Requesting...",
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            Toast.makeText(getActivity(),
                                    String.format(getString(R.string.prompt_prevented_for_permission),
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    Toast.LENGTH_LONG)
                                    .show();
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQCODE_PERM_STORAGE);
                            return false;
                        }
                    }
                    return true;
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener((SettingsActivity) getActivity());
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener((SettingsActivity) getActivity());
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

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case REQCODE_PERM_STORAGE:
                    if (grantResults.length == 0) {
                        Logger.wtf("Request permission result with ZERO length!!!");
                        return;
                    }
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Logger.i("Request for permission <%s> granted", permissions[0]);
                    } else {
                        Logger.i("Request for permission <%s> denied", permissions[0]);
                    }
            }
        }
    }
}
