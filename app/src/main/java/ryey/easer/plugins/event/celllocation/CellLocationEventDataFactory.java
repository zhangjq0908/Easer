package ryey.easer.plugins.event.celllocation;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class CellLocationEventDataFactory implements EventDataFactory<CellLocationEventData> {
    @NonNull
    @Override
    public Class<CellLocationEventData> dataClass() {
        return CellLocationEventData.class;
    }

    @NonNull
    @Override
    public CellLocationEventData emptyData() {
        return new CellLocationEventData();
    }

    @ValidData
    @NonNull
    @Override
    public CellLocationEventData dummyData() {
        CellLocationEventData dummyData = new CellLocationEventData(new String[]{"1-2", "2-3"});
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public CellLocationEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new CellLocationEventData(data, format, version);
    }
}
