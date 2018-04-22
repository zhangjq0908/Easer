package ryey.easer.plugins.operation.wifi;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class WifiOperationDataFactory implements OperationDataFactory<WifiOperationData> {
    @NonNull
    @Override
    public Class<WifiOperationData> dataClass() {
        return WifiOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public WifiOperationData dummyData() {
        return new WifiOperationData(true);
    }

    @ValidData
    @NonNull
    @Override
    public WifiOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new WifiOperationData(data, format, version);
    }
}
