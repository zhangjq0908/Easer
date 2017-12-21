package ryey.easer.plugins.event.connectivity;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class ConnectivityEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return ConnectivityEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new ConnectivityEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        ConnectivityEventData dummyData = new ConnectivityEventData();
        dummyData.set(new String[]{"1", "2"});
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new ConnectivityEventData(data, format, version);
    }
}
