package ryey.easer.plugins.reusable;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ryey.easer.R;

public class PluginHelper {

    public static boolean useRootFeature(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(
                context.getString(R.string.key_pref_use_root), false);
    }
}
