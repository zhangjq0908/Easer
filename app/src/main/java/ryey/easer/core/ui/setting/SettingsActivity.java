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

package ryey.easer.core.ui.setting;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ryey.easer.BuildConfig;
import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.core.BootupReceiver;
import ryey.easer.core.EHService;
import ryey.easer.core.data.Helper;
import ryey.easer.core.data.InvalidExportedDataException;
import ryey.easer.core.data.storage.StorageHelper;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String BS_NAME_PLUGIN_ENABLED = "bs_plugin_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_pref_autostart))) {
            ComponentName componentName = new ComponentName(this, BootupReceiver.class);
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
        } else if (key.equals(getString(R.string.key_pref_use_root))) {
            if (sharedPreferences.getBoolean(key, false)) {
                try {
                    Process p = Runtime.getRuntime().exec("su");
                } catch (IOException e) {
                    e.printStackTrace();
                    sharedPreferences.edit().putBoolean(key, false).apply();
                }
            }
        }
    }

    public static class SettingsFragment extends PreferenceFragment {

        final static int REQCODE_PICK_FILE = 10;
        final static int REQCODE_PERM_EXPORT = 11;
        final static int REQCODE_PERM_IMPORT = 12;
        final static int REQCODE_PERM_STORAGE = 1;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                String version = pInfo.versionName;
                Preference pref_version = findPreference(getString(R.string.key_pref_version));
                pref_version.setSummary(version);
            } catch (PackageManager.NameNotFoundException e) {
                Logger.e(e, "Unable to get app version");
            }

            Preference pref_export = findPreference(getString(R.string.key_pref_export));
            pref_export.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    if (!Utils.hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQCODE_PERM_EXPORT);
                    } else {
                        export_data(getActivity());
                    }
                    return true;
                }
            });

            Preference pref_import = findPreference(getString(R.string.key_pref_import));
            pref_import.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (!Utils.hasPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQCODE_PERM_IMPORT);
                            return false;
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/zip");
                        startActivityForResult(intent, REQCODE_PICK_FILE);
                    }
                    return true;
                }
            });

            Preference pref_logging = findPreference(getString(R.string.key_pref_logging));
            pref_logging.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if ((Boolean) newValue) {
                        if (!Utils.hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Logger.i("Permission <%s> not granted. Requesting...",
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQCODE_PERM_STORAGE);
                            return false;
                        }
                    }
                    return true;
                }
            });

            findPreference(getString(R.string.key_pref_cooldown))
                    .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            if (BuildConfig.DEBUG && !(newValue instanceof String))
                                throw new AssertionError();
                            String interval_str = (String) newValue;
                            try {
                                int interval = Integer.parseInt(interval_str);
                                if (interval < 0)
                                    throw new NumberFormatException();
                                return true;
                            } catch (NumberFormatException e) {
                                Toast.makeText(getActivity(),
                                        R.string.cooldown_time_illformed, Toast.LENGTH_SHORT)
                                        .show();
                                return false;
                            }
                        }
                    });

            findPreference(getString(R.string.key_pref_plugins))
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    PluginSettingsPreferenceFragment fragment = new PluginSettingsPreferenceFragment();
                    getFragmentManager().beginTransaction()
                            .replace(android.R.id.content, fragment)
                            .addToBackStack(BS_NAME_PLUGIN_ENABLED)
                            .commit();
                    return true;
                }
            });

            findPreference(getString(R.string.key_pref_convert_data))
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (!StorageHelper.convertToNewFormat(getActivity())) {
                        Toast.makeText(getActivity(), R.string.message_convert_data_error, Toast.LENGTH_LONG).show();
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
            if (requestCode == REQCODE_PICK_FILE) {
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        Helper.import_data(getActivity(), uri);
                        EHService.reload(getActivity());
                    } catch (IOException e) {
                        Logger.e(e, "IOException caught when importing data");
                        e.printStackTrace();
                    } catch (InvalidExportedDataException e) {
                        Toast.makeText(getActivity(),
                                R.string.message_importing_invalid_data,
                                Toast.LENGTH_LONG)
                                .show();
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case REQCODE_PERM_STORAGE:
                case REQCODE_PERM_EXPORT:
                case REQCODE_PERM_IMPORT:
                    if (grantResults.length == 0) {
                        Logger.wtf("Request permission result with ZERO length!!!");
                        return;
                    }
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Logger.i("Request for permission <%s> granted", permissions[0]);
                    } else {
                        Logger.i("Request for permission <%s> denied", permissions[0]);
                    }
                    break;
            }
        }

        private static void export_data(Context context) {
            try {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.hh:mm:ss", Locale.US);
                String time_str = sdf.format(calendar.getTime());
                File sdCard = Environment.getExternalStorageDirectory();
                File dest = new File(sdCard, String.format("Easer.%s.zip", time_str));
                Logger.v("Export destination: %s", dest);
                Helper.export_data(context, dest);
                Toast.makeText(context,
                        String.format(context.getString(R.string.template_export), dest.getName()),
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
