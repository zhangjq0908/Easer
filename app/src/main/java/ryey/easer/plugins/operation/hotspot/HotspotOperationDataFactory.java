package ryey.easer.plugins.operation.hotspot;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class HotspotOperationDataFactory implements OperationDataFactory<HotspotOperationData> {
    @NonNull
    @Override
    public Class<HotspotOperationData> dataClass() {
        return HotspotOperationData.class;
    }

    @NonNull
    @Override
    public HotspotOperationData emptyData() {
        return new HotspotOperationData();
    }

    @ValidData
    @NonNull
    @Override
    public HotspotOperationData dummyData() {
        HotspotOperationData dummyData = new HotspotOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public HotspotOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new HotspotOperationData(data, format, version);
    }
}
