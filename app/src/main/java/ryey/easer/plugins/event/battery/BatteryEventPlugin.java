package ryey.easer.plugins.event.battery;

import android.content.Context;

import ryey.easer.commons.plugindef.ContentFragment;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;

public class BatteryEventPlugin implements EventPlugin {
    static String pname() {
        return "battery";
    }

    @Override
    public String name() {
        return pname();
    }

    @Override
    public EventData data() {
        return new BatteryEventData();
    }

    @Override
    public ContentFragment view() {
        return new BatteryContentFragment();
    }

    @Override
    public AbstractSlot slot(Context context) {
        return new BatterySlot(context);
    }
}
