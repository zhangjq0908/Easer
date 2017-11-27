package ryey.easer.plugins.event.wifi;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class WifiEventDataTest {

    public static WifiEventData createDummyData() {
        WifiEventData dummyData = new WifiEventData();
        dummyData.set(new String[]{"wifi_device1", "wifi_dev2"});
        return dummyData;
    }

    @Test
    public void testParcel() {
        WifiEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        WifiEventData parceledData = WifiEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}