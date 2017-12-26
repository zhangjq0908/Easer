package ryey.easer.plugins.event.bluetooth_device;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class BTDeviceEventDataTest {

    @Test
    public void testParcel() {
        BTDeviceEventData dummyData = new BTDeviceEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BTDeviceEventData parceledData = BTDeviceEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}