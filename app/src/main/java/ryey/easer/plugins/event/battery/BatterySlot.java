package ryey.easer.plugins.event.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public class BatterySlot extends AbstractSlot {

    int status;
    EventType type;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_POWER_CONNECTED:
                    determineAndNotify(true);
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    determineAndNotify(false);
                    break;
            }
        }
    };
    IntentFilter filter;

    {
        filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
//        filter.addAction(Intent.ACTION_BATTERY_LOW);
//        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }

    public BatterySlot(Context context) {
        super(context);
    }

    @Override
    public void set(EventData data) {
        status = (int) data.get();
        type = data.type();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void listen() {
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void cancel() {
        context.unregisterReceiver(receiver);
    }

    @Override
    public void check() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                             status == BatteryManager.BATTERY_STATUS_FULL;
        determineAndNotify(isCharging);
    }

    void determineAndNotify(boolean isCharging) {
        if ((status == BatteryStatus.charging) == isCharging) {
            changeSatisfiedState(type == EventType.is);
        } else {
            changeSatisfiedState(type == EventType.is_not);
        }
    }
}
