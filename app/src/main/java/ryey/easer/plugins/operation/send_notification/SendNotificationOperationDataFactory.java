package ryey.easer.plugins.operation.send_notification;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class SendNotificationOperationDataFactory implements OperationDataFactory<SendNotificationOperationData> {
    @NonNull
    @Override
    public Class<SendNotificationOperationData> dataClass() {
        return SendNotificationOperationData.class;
    }

    @NonNull
    @Override
    public SendNotificationOperationData emptyData() {
        return new SendNotificationOperationData();
    }

    @ValidData
    @NonNull
    @Override
    public SendNotificationOperationData dummyData() {
        NotificationContent notificationContent = new NotificationContent();
        notificationContent.title = "my test title";
        notificationContent.content = "my test content";
        SendNotificationOperationData dummyData = new SendNotificationOperationData(notificationContent);
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public SendNotificationOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new SendNotificationOperationData(data, format, version);
    }
}
