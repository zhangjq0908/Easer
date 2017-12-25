package ryey.easer.commons.plugindef.eventplugin;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.DataFactory;
import ryey.easer.commons.plugindef.ValidData;


public interface EventDataFactory<T extends EventData> extends DataFactory<T> {
    @NonNull
    Class<T> dataClass();

    @NonNull
    T emptyData();

    @ValidData
    @NonNull
    T dummyData();

    @ValidData
    @NonNull
    T parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException;
}
