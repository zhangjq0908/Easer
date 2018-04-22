package ryey.easer.plugins.operation.alarm;

import android.support.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class AlarmOperationDataFactory implements OperationDataFactory<AlarmOperationData> {
    @NonNull
    @Override
    public Class<AlarmOperationData> dataClass() {
        return AlarmOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public AlarmOperationData dummyData() {
        Calendar time = Calendar.getInstance();
        String message = "my message";
        boolean absolute = false;
        return new AlarmOperationData(time, message, absolute);
    }

    @ValidData
    @NonNull
    @Override
    public AlarmOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new AlarmOperationData(data, format, version);
    }
}
