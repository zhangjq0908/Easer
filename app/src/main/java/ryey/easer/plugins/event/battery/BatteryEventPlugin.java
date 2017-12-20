package ryey.easer.plugins.event.battery;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;

public class BatteryEventPlugin implements EventPlugin {

    @NonNull
    @Override
    public String id() {
        return "battery";
    }

    @Override
    public int name() {
        return R.string.event_battery;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Override
    public boolean checkPermissions(@NonNull Context context) {
        return true;
    }

    @Override
    public void requestPermissions(@NonNull Activity activity, int requestCode) {

    }

    @NonNull
    @Override
    public EventData data() {
        return new BatteryEventData();
    }

    @NonNull
    @Override
    public PluginViewFragment view() {
        return new BatteryPluginViewFragment();
    }

    @Override
    public AbstractSlot slot(Context context) {
        return new BatterySlot(context);
    }
}
