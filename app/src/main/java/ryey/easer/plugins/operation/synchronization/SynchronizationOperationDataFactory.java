package ryey.easer.plugins.operation.synchronization;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class SynchronizationOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return SynchronizationOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new SynchronizationOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        SynchronizationOperationData dummyData = new SynchronizationOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new SynchronizationOperationData(data, format, version);
    }
}
