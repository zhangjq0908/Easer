package ryey.easer.plugins.operation.wifi;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class WifiOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return WifiOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new WifiOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        WifiOperationData dummyData = new WifiOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new WifiOperationData(data, format, version);
    }
}
