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

package ryey.easer;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Environment;
import android.preference.PreferenceManager;

import androidx.core.content.ContextCompat;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraToast;

import java.io.File;

import ryey.easer.core.log.ActivityLogService;

@AcraCore(buildConfigClass = BuildConfig.class,
        reportSenderFactoryClasses = ErrorSenderFactory.class)
@AcraToast(resText=R.string.prompt_error_logged)
public class EaserApplication extends Application {

    static final String LOG_DIR = new File(Environment.getExternalStorageDirectory(), "/logger/error").getAbsolutePath();

    private final LocaleHelperApplicationDelegate localeAppDelegate = new LocaleHelperApplicationDelegate();

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.addLogAdapter(new AndroidLogAdapter());

        if (SettingsHelper.logging(this)) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Logger.addLogAdapter(new DiskLogAdapter());
            } else {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean(getString(R.string.key_pref_logging), false)
                        .apply();
            }
        }

        startService(new Intent(this, ActivityLogService.class));

        Logger.log(Logger.ASSERT, null, "======Easer started======", null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeAppDelegate.onConfigurationChanged(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(localeAppDelegate.attachBaseContext(base));
        ACRA.init(this);
    }
}
