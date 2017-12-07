package ryey.easer.plugins.operation.send_notification;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class SendNotificationOperationDataTest {

    public static SendNotificationOperationData createDummyData() {
        SendNotificationOperationData dummyData = new SendNotificationOperationData();
        NotificationContent notificationContent = new NotificationContent();
        notificationContent.title = "my test title";
        notificationContent.content = "my test content";
        dummyData.set(notificationContent);
        return dummyData;
    }

    @Test
    public void testParcel() {
        SendNotificationOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        SendNotificationOperationData parceledData = SendNotificationOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}