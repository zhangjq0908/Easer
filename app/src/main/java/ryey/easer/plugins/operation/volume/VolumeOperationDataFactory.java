package ryey.easer.plugins.operation.volume;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class VolumeOperationDataFactory implements OperationDataFactory<VolumeOperationData> {
    @NonNull
    @Override
    public Class<VolumeOperationData> dataClass() {
        return VolumeOperationData.class;
    }

    @NonNull
    @Override
    public VolumeOperationData emptyData() {
        return new VolumeOperationData();
    }

    @ValidData
    @NonNull
    @Override
    public VolumeOperationData dummyData() {
        return new VolumeOperationData(1, 2, null, 0);
    }

    @ValidData
    @NonNull
    @Override
    public VolumeOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new VolumeOperationData(data, format, version);
    }
}
