package ryey.easer.plugins.event.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.annotation.NonNull;

import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public class BatterySlot extends AbstractSlot<BatteryEventData> {

    private int status;
    private EventType type;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
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
    private IntentFilter filter;

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
    public void set(@ValidData @NonNull BatteryEventData data) {
        status = data.battery_status;
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

    private void determineAndNotify(boolean isCharging) {
        if ((status == BatteryStatus.charging) == isCharging) {
            changeSatisfiedState(type == EventType.is);
        } else {
            changeSatisfiedState(type == EventType.is_not);
        }
    }
}
