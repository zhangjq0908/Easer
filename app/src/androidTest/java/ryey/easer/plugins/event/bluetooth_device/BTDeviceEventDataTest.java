package ryey.easer.plugins.event.bluetooth_device;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class BTDeviceEventDataTest {

    public static BTDeviceEventData createDummyData() {
        BTDeviceEventData dummyData = new BTDeviceEventData();
        dummyData.set(new String[]{"device1", "dev2"});
        return dummyData;
    }

    @Test
    public void testParcel() {
        BTDeviceEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BTDeviceEventData parceledData = BTDeviceEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}