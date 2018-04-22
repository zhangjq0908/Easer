package ryey.easer.plugins.operation.media_control;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class MediaControlOperationDataFactory implements OperationDataFactory<MediaControlOperationData> {
    @NonNull
    @Override
    public Class<MediaControlOperationData> dataClass() {
        return MediaControlOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public MediaControlOperationData dummyData() {
        MediaControlOperationData dummyData = new MediaControlOperationData(MediaControlOperationData.ControlChoice.next);
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public MediaControlOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new MediaControlOperationData(data, format, version);
    }
}
