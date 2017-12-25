package ryey.easer.plugins.operation.command;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class CommandOperationDataFactory implements OperationDataFactory<CommandOperationData> {
    @NonNull
    @Override
    public Class<CommandOperationData> dataClass() {
        return CommandOperationData.class;
    }

    @NonNull
    @Override
    public CommandOperationData emptyData() {
        return new CommandOperationData();
    }

    @NonNull
    @Override
    public CommandOperationData dummyData() {
        CommandOperationData dummyData = new CommandOperationData();
        dummyData.set("/sdcard/mycmd_file");
        return dummyData;
    }

    @NonNull
    @Override
    public CommandOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new CommandOperationData(data, format, version);
    }
}
