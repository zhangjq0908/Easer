package ryey.easer.plugins.event.connectivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.plugins.reusable.PluginHelper;

public class ConnectivityEventPlugin implements EventPlugin {

    @NonNull
    @Override
    public String id() {
        return "connectivity";
    }

    @Override
    public int name() {
        return R.string.event_connectivity;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return PluginHelper.checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {
        PluginHelper.requestPermission(activity, requestCode, Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @NonNull
    @Override
    public EventDataFactory dataFactory() {
        return new ConnectivityEventDataFactory();
    }

    @NonNull
    @Override
    public PluginViewFragment view() {
        return new ConnectivityPluginViewFragment();
    }

    @Override
    public AbstractSlot slot(Context context) {
        return new ConnectivitySlot(context);
    }

}
