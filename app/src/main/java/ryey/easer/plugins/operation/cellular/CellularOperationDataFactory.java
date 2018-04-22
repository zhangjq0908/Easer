package ryey.easer.plugins.operation.cellular;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class CellularOperationDataFactory implements OperationDataFactory<CellularOperationData> {
    @NonNull
    @Override
    public Class<CellularOperationData> dataClass() {
        return CellularOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public CellularOperationData dummyData() {
        return new CellularOperationData(true);
    }

    @ValidData
    @NonNull
    @Override
    public CellularOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new CellularOperationData(data, format, version);
    }
}
