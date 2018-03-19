package ryey.easer.plugins.event.battery;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.TestHelper;

import static org.junit.Assert.assertEquals;

public class BatteryEventDataTest {

    @Test
    public void testParcel() {
        BatteryEventData dummyData = new BatteryEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BatteryEventData parceledData = BatteryEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}