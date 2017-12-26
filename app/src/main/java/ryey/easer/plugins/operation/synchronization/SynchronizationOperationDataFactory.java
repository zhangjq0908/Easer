package ryey.easer.plugins.operation.synchronization;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class SynchronizationOperationDataFactory implements OperationDataFactory<SynchronizationOperationData> {
    @NonNull
    @Override
    public Class<SynchronizationOperationData> dataClass() {
        return SynchronizationOperationData.class;
    }

    @NonNull
    @Override
    public SynchronizationOperationData emptyData() {
        return new SynchronizationOperationData();
    }

    @ValidData
    @NonNull
    @Override
    public SynchronizationOperationData dummyData() {
        SynchronizationOperationData dummyData = new SynchronizationOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public SynchronizationOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new SynchronizationOperationData(data, format, version);
    }
}
