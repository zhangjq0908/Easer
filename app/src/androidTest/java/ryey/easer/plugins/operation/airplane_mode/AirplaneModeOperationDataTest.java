package ryey.easer.plugins.operation.airplane_mode;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class AirplaneModeOperationDataTest {

    @Test
    public void testParcel() {
        AirplaneModeOperationData dummyData = (AirplaneModeOperationData) new AirplaneModeOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        AirplaneModeOperationData parceledData = AirplaneModeOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }
}