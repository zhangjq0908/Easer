package ryey.easer.plugins.event.battery;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import ryey.easer.R;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.PluginViewFragment;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;
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
    public EventDataFactory dataFactory() {
        return new EventDataFactory() {
            @NonNull
            @Override
            public Class<? extends EventData> dataClass() {
                return BatteryEventData.class;
            }

            @NonNull
            @Override
            public EventData emptyData() {
                return new BatteryEventData();
            }

            @NonNull
            @Override
            public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
                return new BatteryEventData(data, format, version);
            }
        };
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
