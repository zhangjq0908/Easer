package ryey.easer.core;

import android.content.Context;
import android.preference.PreferenceManager;

import ryey.easer.R;

public class SettingsHelper {

    public static int coolDownInterval(Context context) {
        String interval_pref = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.key_pref_cooldown), "3");
        return Integer.parseInt(interval_pref);
    }

}
