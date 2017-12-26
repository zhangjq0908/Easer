package ryey.easer.plugins.event.sms;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class SmsEventDataTest {

    @Test
    public void testParcel() {
        SmsEventData dummyData = new SmsEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        SmsEventData parceledData = SmsEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}