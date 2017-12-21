package ryey.easer.plugins.event.battery;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class BatteryEventDataFactory implements EventDataFactory {
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
    public EventData dummyData() {
        BatteryEventData dummyData = new BatteryEventData();
        dummyData.set(1);
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new BatteryEventData(data, format, version);
    }
}
