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

    @NonNull
    @Override
    public AlarmOperationData emptyData() {
        return new AlarmOperationData();
    }

    @ValidData
    @NonNull
    @Override
    public AlarmOperationData dummyData() {
        AlarmOperationData.AlarmData alarmData = new AlarmOperationData.AlarmData();
        alarmData.time = Calendar.getInstance();
        alarmData.message = "my message";
        alarmData.absolute = false;
        AlarmOperationData dummyData = new AlarmOperationData(alarmData);
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public AlarmOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new AlarmOperationData(data, format, version);
    }
}
