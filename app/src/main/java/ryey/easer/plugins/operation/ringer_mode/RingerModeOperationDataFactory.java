package ryey.easer.plugins.operation.ringer_mode;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class RingerModeOperationDataFactory implements OperationDataFactory<RingerModeOperationData> {
    @NonNull
    @Override
    public Class<RingerModeOperationData> dataClass() {
        return RingerModeOperationData.class;
    }

    @NonNull
    @Override
    public RingerModeOperationData emptyData() {
        return new RingerModeOperationData();
    }

    @NonNull
    @Override
    public RingerModeOperationData dummyData() {
        RingerModeOperationData dummyData = new RingerModeOperationData();
        dummyData.set(1);
        return dummyData;
    }

    @NonNull
    @Override
    public RingerModeOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new RingerModeOperationData(data, format, version);
    }
}
