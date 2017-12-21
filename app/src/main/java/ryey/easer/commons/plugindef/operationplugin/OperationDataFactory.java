package ryey.easer.commons.plugindef.operationplugin;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.DataFactory;


public interface OperationDataFactory extends DataFactory {
    @NonNull
    Class<? extends OperationData> dataClass();

    @NonNull
    OperationData emptyData();

    @NonNull
    OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException;
}
