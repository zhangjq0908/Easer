package ryey.easer.plugins.event.connectivity;

import android.content.Context;

import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;

public class ConnectivityEventPlugin implements EventPlugin {

    @Override
    public String name() {
        return "connectivity";
    }

    @Override
    public EventData data() {
        return new ConnectivityEventData();
    }

    @Override
    public PluginViewFragment view() {
        return new ConnectivityPluginViewFragment();
    }

    @Override
    public AbstractSlot slot(Context context) {
        return new ConnectivitySlot(context);
    }
}
