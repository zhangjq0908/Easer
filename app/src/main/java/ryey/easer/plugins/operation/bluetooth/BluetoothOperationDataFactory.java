package ryey.easer.plugins.operation.bluetooth;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class BluetoothOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return BluetoothOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new BluetoothOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        BluetoothOperationData dummyData = new BluetoothOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new BluetoothOperationData(data, format, version);
    }
}
