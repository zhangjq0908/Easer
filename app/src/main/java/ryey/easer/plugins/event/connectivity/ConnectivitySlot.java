package ryey.easer.plugins.event.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.util.Set;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

import static ryey.easer.plugins.event.connectivity.ConnectivityType.TYPE_BLUETOOTH;
import static ryey.easer.plugins.event.connectivity.ConnectivityType.TYPE_ETHERNET;
import static ryey.easer.plugins.event.connectivity.ConnectivityType.TYPE_MOBILE;
import static ryey.easer.plugins.event.connectivity.ConnectivityType.TYPE_NOT_CONNECTED;
import static ryey.easer.plugins.event.connectivity.ConnectivityType.TYPE_VPN;
import static ryey.easer.plugins.event.connectivity.ConnectivityType.TYPE_WIFI;

class ConnectivitySlot extends AbstractSlot<ConnectivityEventData> {

    private Set<Integer> connectivity_types;
    private EventType type;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    check();
                    break;
            }
        }
    };
    private final IntentFilter filter;

    {
        filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public ConnectivitySlot(Context context) {
        super(context);
    }

    @Override
    public void set(@NonNull ConnectivityEventData data) {
        connectivity_types = ((ConnectivityEventData) data).connectivity_type;
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
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        determineAndNotify(convertType(activeNetworkInfo));
    }

    private int convertType(NetworkInfo activeNetworkInfo) {
        if (activeNetworkInfo == null) {
            return TYPE_NOT_CONNECTED;
        }
        switch (activeNetworkInfo.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                return TYPE_WIFI;
            case ConnectivityManager.TYPE_MOBILE:
                return TYPE_MOBILE;
            case ConnectivityManager.TYPE_ETHERNET:
                return TYPE_ETHERNET;
            case ConnectivityManager.TYPE_BLUETOOTH:
                return TYPE_BLUETOOTH;
            case ConnectivityManager.TYPE_VPN:
                return TYPE_VPN;
        }
        return -1;
    }

    private void determineAndNotify(int networkType) {
        if (type == EventType.any) {
            if (connectivity_types.contains(networkType))
                changeSatisfiedState(true);
            else
                changeSatisfiedState(false);
        } else {
            if (connectivity_types.contains(networkType))
                changeSatisfiedState(false);
            else
                changeSatisfiedState(true);
        }
    }
}
