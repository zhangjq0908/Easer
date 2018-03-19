package ryey.easer.plugins.event.notification;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.TestHelper;

import static org.junit.Assert.assertEquals;

public class NotificationEventDataTest {

    @Test
    public void testParcel() {
        NotificationEventData dummyData = new NotificationEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        NotificationEventData parceledData = NotificationEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}