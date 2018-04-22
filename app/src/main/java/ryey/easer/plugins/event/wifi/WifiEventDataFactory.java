package ryey.easer.plugins.event.wifi;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class WifiEventDataFactory implements EventDataFactory<WifiEventData> {
    @NonNull
    @Override
    public Class<WifiEventData> dataClass() {
        return WifiEventData.class;
    }

    @ValidData
    @NonNull
    @Override
    public WifiEventData dummyData() {
        return new WifiEventData("wifi_device1\nwifi_dev2", true);
    }

    @ValidData
    @NonNull
    @Override
    public WifiEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new WifiEventData(data, format, version);
    }
}
