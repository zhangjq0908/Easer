package ryey.easer.plugins.operation.send_notification;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class SendNotificationOperationDataFactory implements OperationDataFactory {
    @NonNull
    @Override
    public Class<? extends OperationData> dataClass() {
        return SendNotificationOperationData.class;
    }

    @NonNull
    @Override
    public OperationData emptyData() {
        return new SendNotificationOperationData();
    }

    @NonNull
    @Override
    public OperationData dummyData() {
        NotificationContent notificationContent = new NotificationContent();
        notificationContent.title = "my test title";
        notificationContent.content = "my test content";
        SendNotificationOperationData dummyData = new SendNotificationOperationData(notificationContent);
        return dummyData;
    }

    @NonNull
    @Override
    public OperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new SendNotificationOperationData(data, format, version);
    }
}
