package ryey.easer.plugins.event.nfc_tag;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class NfcTagEventDataTest {

    @Test
    public void testParcel() {
        NfcTagEventData dummyData = new NfcTagEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        NfcTagEventData parceledData = NfcTagEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}