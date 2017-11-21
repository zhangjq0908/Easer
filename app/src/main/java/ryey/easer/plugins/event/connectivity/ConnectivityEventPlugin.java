package ryey.easer.plugins.event.connectivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.plugins.reusable.PluginHelper;

public class ConnectivityEventPlugin implements EventPlugin {

    @Override
    public String name() {
        return "connectivity";
    }

    @Override
    public boolean checkPermissions(Context context) {
        return PluginHelper.checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @Override
    public void requestPermissions(Activity activity, int requestCode) {
        PluginHelper.requestPermission(activity, requestCode, Manifest.permission.ACCESS_NETWORK_STATE);
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
