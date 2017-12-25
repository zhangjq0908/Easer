package ryey.easer.plugins.event.celllocation;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class CellLocationEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return CellLocationEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new CellLocationEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        CellLocationEventData dummyData = new CellLocationEventData(new String[]{"1-2", "2-3"});
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new CellLocationEventData(data, format, version);
    }
}
