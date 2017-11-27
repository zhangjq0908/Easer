package ryey.easer.plugins.event.battery;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class BatteryEventDataTest {

    public static BatteryEventData createDummyData() {
        BatteryEventData dummyData = new BatteryEventData();
        dummyData.set(1);
        return dummyData;
    }

    @Test
    public void testParcel() {
        BatteryEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BatteryEventData parceledData = BatteryEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}