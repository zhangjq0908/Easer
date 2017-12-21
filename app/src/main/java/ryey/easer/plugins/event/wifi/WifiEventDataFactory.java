package ryey.easer.plugins.event.wifi;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class WifiEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return WifiEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new WifiEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        WifiEventData dummyData = new WifiEventData();
        dummyData.set(new String[]{"wifi_device1", "wifi_dev2"});
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new WifiEventData(data, format, version);
    }
}
