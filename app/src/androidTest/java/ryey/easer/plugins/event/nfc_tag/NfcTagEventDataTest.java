package ryey.easer.plugins.event.nfc_tag;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class NfcTagEventDataTest {

    public static NfcTagEventData createDummyData() {
        NfcTagEventData dummyData = new NfcTagEventData();
        dummyData.set("01479362");
        return dummyData;
    }

    @Test
    public void testParcel() {
        NfcTagEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        NfcTagEventData parceledData = NfcTagEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}