package ryey.easer.commons.plugindef.eventplugin;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.DataFactory;


public interface EventDataFactory extends DataFactory {
    @NonNull
    Class<? extends EventData> dataClass();

    @NonNull
    EventData emptyData();

    @NonNull
    EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException;
}
