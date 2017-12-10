package ryey.easer.commons;

import java.util.Locale;

import ryey.easer.commons.plugindef.PluginDef;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;

public class CommonHelper {
    public static String pluginEnabledKey(PluginDef plugin) {
        String type_name;
        if (plugin instanceof OperationPlugin)
            type_name = "operation";
        else if (plugin instanceof EventPlugin)
            type_name = "event";
        else
            throw new IllegalAccessError();
        return String.format(Locale.US, "%s_plugin_enabled_%s", type_name, plugin.name());
    }
}
