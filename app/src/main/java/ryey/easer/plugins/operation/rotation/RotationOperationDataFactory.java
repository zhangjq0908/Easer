package ryey.easer.plugins.operation.rotation;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class RotationOperationDataFactory implements OperationDataFactory<RotationOperationData> {
    @NonNull
    @Override
    public Class<RotationOperationData> dataClass() {
        return RotationOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public RotationOperationData dummyData() {
        return new RotationOperationData(true);
    }

    @ValidData
    @NonNull
    @Override
    public RotationOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new RotationOperationData(data, format, version);
    }
}
