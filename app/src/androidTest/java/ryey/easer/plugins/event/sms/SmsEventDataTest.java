package ryey.easer.plugins.event.sms;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class SmsEventDataTest {

    public static SmsEventData createDummyData() {
        SmsEventData dummyData = new SmsEventData();
        SmsInnerData innerData = new SmsInnerData();
        innerData.sender = "15077707777";
        innerData.content = "aaa";
        dummyData.set(innerData);
        return dummyData;
    }

    @Test
    public void testParcel() {
        SmsEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        SmsEventData parceledData = SmsEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}