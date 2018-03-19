package ryey.easer.plugins.event.wifi;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.TestHelper;

import static org.junit.Assert.assertEquals;

public class WifiEventDataTest {

    @Test
    public void testParcel() {
        WifiEventData dummyData = new WifiEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        WifiEventData parceledData = WifiEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}