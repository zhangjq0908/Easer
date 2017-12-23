package ryey.easer.plugins.event.connectivity;

import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;

import java.util.Set;

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
        Set<Integer> data = new ArraySet<>();
        data.add(1);
        data.add(2);
        ConnectivityEventData dummyData = new ConnectivityEventData(data);
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new ConnectivityEventData(data, format, version);
    }
}
