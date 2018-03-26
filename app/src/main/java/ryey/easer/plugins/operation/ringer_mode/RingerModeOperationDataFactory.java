package ryey.easer.plugins.operation.ringer_mode;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
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

    @ValidData
    @NonNull
    @Override
    public RingerModeOperationData dummyData() {
        return new RingerModeOperationData(RingerMode.vibrate);
    }

    @ValidData
    @NonNull
    @Override
    public RingerModeOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new RingerModeOperationData(data, format, version);
    }
}
