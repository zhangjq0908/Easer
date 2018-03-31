package ryey.easer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsHelper {

    private static SharedPreferences pref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int coolDownInterval(Context context) {
        String interval_pref = pref(context).getString(context.getString(R.string.key_pref_cooldown), "3");
        return Integer.parseInt(interval_pref);
    }

    public static boolean passiveMode(Context context) {
        return pref(context).getBoolean(context.getString(R.string.key_pref_passive_mode), true);
    }

    public static boolean logging(Context context) {
        return pref(context).getBoolean(context.getString(R.string.key_pref_logging), false);
    }

    public static boolean use12HourClock(Context context) {
        return pref(context).getBoolean(context.getString(R.string.key_pref_use_12_hour_clock), false);
    }

}
