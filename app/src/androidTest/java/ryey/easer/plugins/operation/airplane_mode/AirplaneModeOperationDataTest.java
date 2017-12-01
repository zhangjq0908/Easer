package ryey.easer.plugins.operation.airplane_mode;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class AirplaneModeOperationDataTest {
    public static AirplaneModeOperationData createDummyData() {
        AirplaneModeOperationData dummyData = new AirplaneModeOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @Test
    public void testParcel() {
        AirplaneModeOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        AirplaneModeOperationData parceledData = AirplaneModeOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }
}