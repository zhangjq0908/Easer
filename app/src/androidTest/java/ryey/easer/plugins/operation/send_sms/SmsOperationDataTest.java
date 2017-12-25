package ryey.easer.plugins.operation.send_sms;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class SmsOperationDataTest {

    @Test
    public void testParcel() {
        SmsOperationData dummyData = new SendSmsOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        SmsOperationData parceledData = SmsOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}