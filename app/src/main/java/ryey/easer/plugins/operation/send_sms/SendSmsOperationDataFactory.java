package ryey.easer.plugins.operation.send_sms;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class SendSmsOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return SmsOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new SmsOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        SmsOperationData dummyData = new SmsOperationData();
        Sms sms = new Sms();
        sms.destination = "15077707777";
        sms.content = "mysmscontent";
        dummyData.set(sms);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new SmsOperationData(data, format, version);
    }
}
