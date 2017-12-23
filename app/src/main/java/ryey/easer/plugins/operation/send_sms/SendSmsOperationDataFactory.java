package ryey.easer.plugins.operation.send_sms;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class SendSmsOperationDataFactory implements OperationDataFactory<SmsOperationData> {
    @NonNull
    @Override
    public Class<SmsOperationData> dataClass() {
        return SmsOperationData.class;
    }

    @NonNull
    @Override
    public SmsOperationData emptyData() {
        return new SmsOperationData();
    }

    @NonNull
    @Override
    public SmsOperationData dummyData() {
        Sms sms = new Sms();
        sms.destination = "15077707777";
        sms.content = "mysmscontent";
        SmsOperationData dummyData = new SmsOperationData(sms);
        return dummyData;
    }

    @NonNull
    @Override
    public SmsOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new SmsOperationData(data, format, version);
    }
}
