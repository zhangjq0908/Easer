package ryey.easer;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

import ryey.easer.plugins.PluginRegistry;

public class EaserApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PluginRegistry.init();

        Logger.addLogAdapter(new AndroidLogAdapter());

        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
                getBoolean(getString(R.string.key_pref_logging), false)) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Logger.addLogAdapter(new DiskLogAdapter());
            } else {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean(getString(R.string.key_pref_logging), false)
                        .apply();
            }
        }

        Logger.log(Logger.ASSERT, null, "======Easer started======", null);
    }
}
