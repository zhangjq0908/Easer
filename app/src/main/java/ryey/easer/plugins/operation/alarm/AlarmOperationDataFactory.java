package ryey.easer.plugins.operation.alarm;

import android.support.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class AlarmOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return AlarmOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new AlarmOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        AlarmOperationData dummyData = new AlarmOperationData();
        AlarmOperationData.AlarmData alarmData = new AlarmOperationData.AlarmData();
        alarmData.time = Calendar.getInstance();
        alarmData.message = "my message";
        alarmData.absolute = false;
        dummyData.set(alarmData);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new AlarmOperationData(data, format, version);
    }
}
