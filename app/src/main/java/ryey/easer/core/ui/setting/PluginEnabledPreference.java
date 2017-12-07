package ryey.easer.core.ui.setting;

import android.content.Context;
import android.preference.CheckBoxPreference;

import java.util.Locale;

import ryey.easer.commons.plugindef.PluginDef;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;

public class PluginEnabledPreference extends CheckBoxPreference {
    static PluginEnabledPreference createInstance(Context context, PluginDef plugin) {
        PluginEnabledPreference preference = new PluginEnabledPreference(context);
        preference.setKey(enabledKey(plugin));
        preference.setTitle(plugin.name());
        return preference;
    }

    public static String enabledKey(PluginDef plugin) {
        String type_name;
        if (plugin instanceof OperationPlugin)
            type_name = "operation";
        else if (plugin instanceof EventPlugin)
            type_name = "event";
        else
            throw new IllegalAccessError();
        return String.format(Locale.US, "%s_plugin_enabled_%s", type_name, plugin.name());
    }

    public PluginEnabledPreference(Context context) {
        super(context);
        setDefaultValue(true);
    }
}
