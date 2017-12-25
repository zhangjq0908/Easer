package ryey.easer.plugins.operation.cellular;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class CellularOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return CellularOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new CellularOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        CellularOperationData dummyData = new CellularOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new CellularOperationData(data, format, version);
    }
}
