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

    @ValidData
    @NonNull
    @Override
    public HotspotOperationData dummyData() {
        return new HotspotOperationData(true);
    }

    @ValidData
    @NonNull
    @Override
    public HotspotOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new HotspotOperationData(data, format, version);
    }
}
