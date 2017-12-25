package ryey.easer.plugins.operation.wifi;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class WifiOperationDataFactory implements OperationDataFactory<WifiOperationData> {
    @NonNull
    @Override
    public Class<WifiOperationData> dataClass() {
        return WifiOperationData.class;
    }

    @NonNull
    @Override
    public WifiOperationData emptyData() {
        return new WifiOperationData();
    }

    @NonNull
    @Override
    public WifiOperationData dummyData() {
        WifiOperationData dummyData = new WifiOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @NonNull
    @Override
    public WifiOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new WifiOperationData(data, format, version);
    }
}
