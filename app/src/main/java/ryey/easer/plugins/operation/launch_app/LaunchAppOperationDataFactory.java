package ryey.easer.plugins.operation.launch_app;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class LaunchAppOperationDataFactory implements OperationDataFactory<LaunchAppOperationData> {
    @NonNull
    @Override
    public Class<LaunchAppOperationData> dataClass() {
        return LaunchAppOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public LaunchAppOperationData dummyData() {
        return new LaunchAppOperationData("com.dummy.app.package");
    }

    @ValidData
    @NonNull
    @Override
    public LaunchAppOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new LaunchAppOperationData(data, format, version);
    }
}
