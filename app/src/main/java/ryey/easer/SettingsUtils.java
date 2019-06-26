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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class SettingsUtils {

    private static SharedPreferences pref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int coolDownInterval(Context context) {
        String interval_pref = pref(context).getString(context.getString(R.string.key_pref_cooldown), "3");
        return Integer.parseInt(interval_pref);
    }

    public static boolean logging(Context context) {
        return pref(context).getBoolean(context.getString(R.string.key_pref_logging), false);
    }

    public static boolean use12HourClock(Context context) {
        return pref(context).getBoolean(context.getString(R.string.key_pref_use_12_hour_clock), false);
    }

    public static boolean showNotification(Context context) {
        return pref(context).getBoolean(context.getString(R.string.key_pref_show_notification), true);
    }

    public static boolean runInForeground(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            return true;
        return pref(context).getBoolean(context.getString(R.string.key_pref_foreground), true);
    }

}
