package ryey.easer.plugins.operation.brightness;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class BrightnessOperationDataFactory implements OperationDataFactory<BrightnessOperationData> {
    @NonNull
    @Override
    public Class<BrightnessOperationData> dataClass() {
        return BrightnessOperationData.class;
    }

    @NonNull
    @Override
    public BrightnessOperationData emptyData() {
        return new BrightnessOperationData();
    }

    @NonNull
    @Override
    public BrightnessOperationData dummyData() {
        BrightnessOperationData dummyData = new BrightnessOperationData();
        dummyData.set(3);
        return dummyData;
    }

    @NonNull
    @Override
    public BrightnessOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new BrightnessOperationData(data, format, version);
    }
}
