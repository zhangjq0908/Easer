package ryey.easer.plugins.operation.airplane_mode;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class AirplaneModeOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return AirplaneModeOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new AirplaneModeOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        AirplaneModeOperationData dummyData = new AirplaneModeOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new AirplaneModeOperationData(data, format, version);
    }
}
