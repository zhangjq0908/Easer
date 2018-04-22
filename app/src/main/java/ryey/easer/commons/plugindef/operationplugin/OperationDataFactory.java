package ryey.easer.commons.plugindef.operationplugin;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.DataFactory;
import ryey.easer.commons.plugindef.ValidData;


public interface OperationDataFactory<T extends OperationData> extends DataFactory<T> {
    @NonNull
    Class<T> dataClass();

    @ValidData
    @NonNull
    T dummyData();

    @ValidData
    @NonNull
    T parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException;
}
