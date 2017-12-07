package ryey.easer.plugins.event.notification;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class NotificationEventDataTest {

    public static NotificationEventData createDummyData() {
        NotificationEventData dummyData = new NotificationEventData();
        NotificationSelection notificationSelection = new NotificationSelection();
        notificationSelection.app = "example.app";
        notificationSelection.title = "title example";
        notificationSelection.content = "content example";
        dummyData.set(notificationSelection);
        return dummyData;
    }

    @Test
    public void testParcel() {
        NotificationEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        NotificationEventData parceledData = NotificationEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}