package ryey.easer.plugins.event.connectivity;

import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;

import java.util.Set;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class ConnectivityEventDataFactory implements EventDataFactory<ConnectivityEventData> {
    @NonNull
    @Override
    public Class<ConnectivityEventData> dataClass() {
        return ConnectivityEventData.class;
    }

    @NonNull
    @Override
    public ConnectivityEventData emptyData() {
        return new ConnectivityEventData();
    }

    @NonNull
    @Override
    public ConnectivityEventData dummyData() {
        Set<Integer> data = new ArraySet<>();
        data.add(1);
        data.add(2);
        ConnectivityEventData dummyData = new ConnectivityEventData(data);
        return dummyData;
    }

    @NonNull
    @Override
    public ConnectivityEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new ConnectivityEventData(data, format, version);
    }
}
