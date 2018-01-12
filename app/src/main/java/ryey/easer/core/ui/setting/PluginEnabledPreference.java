package ryey.easer.core.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.Preference;

import ryey.easer.BuildConfig;
import ryey.easer.R;
import ryey.easer.commons.CommonHelper;
import ryey.easer.commons.plugindef.PluginDef;

class PluginEnabledPreference extends CheckBoxPreference implements Preference.OnPreferenceChangeListener {

    private static final int REQCODE = 2333;

    private final PluginDef plugin;

    PluginEnabledPreference(Context context, PluginDef plugin) {
        super(context);
        this.plugin = plugin;
        setOnPreferenceChangeListener(this);
        setKey(CommonHelper.pluginEnabledKey(plugin));
        setTitle(plugin.name());
        boolean isCompatible = plugin.isCompatible(context);
        if (isCompatible) {
            setDefaultValue(true);
        } else {
            setDefaultValue(false);
            setEnabled(false);
            setSummary(R.string.message_plugin_not_compatible);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (BuildConfig.DEBUG && !(newValue instanceof Boolean)) throw new AssertionError();
        if ((Boolean) newValue && !plugin.checkPermissions(getContext())) {
            plugin.requestPermissions((Activity) getContext(), REQCODE);
            return false;
        }
        return true;
    }
}
