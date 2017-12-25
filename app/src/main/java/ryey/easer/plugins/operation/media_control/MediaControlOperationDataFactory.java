package ryey.easer.plugins.operation.media_control;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class MediaControlOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return MediaControlOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new MediaControlOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        MediaControlOperationData dummyData = new MediaControlOperationData(MediaControlOperationData.ControlChoice.next);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new MediaControlOperationData(data, format, version);
    }
}
