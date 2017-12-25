package ryey.easer.commons.plugindef.eventplugin;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.DataFactory;


public interface EventDataFactory<T extends EventData> extends DataFactory<T> {
    @NonNull
    Class<T> dataClass();

    @NonNull
    T emptyData();

    @NonNull
    T dummyData();

    @NonNull
    T parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException;
}
