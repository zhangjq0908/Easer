package ryey.easer.plugins.operation.hotspot;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class HotspotOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return HotspotOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new HotspotOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        HotspotOperationData dummyData = new HotspotOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new HotspotOperationData(data, format, version);
    }
}
