package ryey.easer.plugins.operation.send_sms;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class SmsOperationDataTest {

    public static SmsOperationData createDummyData() {
        SmsOperationData dummyData = new SmsOperationData();
        Sms sms = new Sms();
        sms.destination = "15077707777";
        sms.content = "mysmscontent";
        dummyData.set(sms);
        return dummyData;
    }

    @Test
    public void testParcel() {
        SmsOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        SmsOperationData parceledData = SmsOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}