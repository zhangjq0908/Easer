package ryey.easer.plugins.event.bluetooth_device;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class BTDeviceEventDataFactory implements EventDataFactory<BTDeviceEventData> {
    @NonNull
    @Override
    public Class<BTDeviceEventData> dataClass() {
        return BTDeviceEventData.class;
    }

    @NonNull
    @Override
    public BTDeviceEventData emptyData() {
        return new BTDeviceEventData();
    }

    @NonNull
    @Override
    public BTDeviceEventData dummyData() {
        BTDeviceEventData dummyData = new BTDeviceEventData(new String[]{"device1", "dev2"});
        return dummyData;
    }

    @NonNull
    @Override
    public BTDeviceEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new BTDeviceEventData(data, format, version);
    }
}
