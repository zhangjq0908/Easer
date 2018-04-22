package ryey.easer.plugins.event.battery;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class BatteryEventDataFactory implements EventDataFactory<BatteryEventData> {
    @NonNull
    @Override
    public Class<BatteryEventData> dataClass() {
        return BatteryEventData.class;
    }

    @ValidData
    @NonNull
    @Override
    public BatteryEventData dummyData() {
        return new BatteryEventData(1);
    }

    @ValidData
    @NonNull
    @Override
    public BatteryEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new BatteryEventData(data, format, version);
    }
}
