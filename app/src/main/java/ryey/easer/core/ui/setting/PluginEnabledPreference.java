package ryey.easer.core.ui.setting;

import android.content.Context;
import android.preference.CheckBoxPreference;

import ryey.easer.R;
import ryey.easer.commons.CommonHelper;
import ryey.easer.commons.plugindef.PluginDef;

class PluginEnabledPreference extends CheckBoxPreference {

    PluginEnabledPreference(Context context, PluginDef plugin) {
        super(context);
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
}
