package ryey.easer.plugins.operation.bluetooth;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class BluetoothOperationDataFactory implements OperationDataFactory<BluetoothOperationData> {
    @NonNull
    @Override
    public Class<BluetoothOperationData> dataClass() {
        return BluetoothOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public BluetoothOperationData dummyData() {
        return new BluetoothOperationData(true);
    }

    @ValidData
    @NonNull
    @Override
    public BluetoothOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new BluetoothOperationData(data, format, version);
    }
}
