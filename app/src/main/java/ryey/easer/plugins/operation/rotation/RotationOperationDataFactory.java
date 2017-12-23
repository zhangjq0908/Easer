package ryey.easer.plugins.operation.rotation;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class RotationOperationDataFactory implements OperationDataFactory<RotationOperationData> {
    @NonNull
    @Override
    public Class<RotationOperationData> dataClass() {
        return RotationOperationData.class;
    }

    @NonNull
    @Override
    public RotationOperationData emptyData() {
        return new RotationOperationData();
    }

    @NonNull
    @Override
    public RotationOperationData dummyData() {
        RotationOperationData dummyData = new RotationOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @NonNull
    @Override
    public RotationOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new RotationOperationData(data, format, version);
    }
}
