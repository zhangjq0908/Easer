package ryey.easer.skills.operation.launch_app;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.operationskill.OperationDataFactory;
import ryey.easer.plugin.PluginDataFormat;

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
        return new LaunchAppOperationData("com.dummy.app.package", "com.dummy.app.package.Activity1");
    }

    @ValidData
    @NonNull
    @Override
    public LaunchAppOperationData parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        return new LaunchAppOperationData(data, format, version);
    }
}
