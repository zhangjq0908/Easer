package ryey.easer.plugins.operation.send_notification;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class SendNotificationOperationDataTest {

    @Test
    public void testParcel() {
        SendNotificationOperationData dummyData = (SendNotificationOperationData) new SendNotificationOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        SendNotificationOperationData parceledData = SendNotificationOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}