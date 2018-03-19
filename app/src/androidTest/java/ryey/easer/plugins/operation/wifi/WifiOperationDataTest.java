package ryey.easer.plugins.operation.wifi;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.TestHelper;

import static org.junit.Assert.assertEquals;

public class WifiOperationDataTest {

    @Test
    public void testParcel() {
        WifiOperationData dummyData = new WifiOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        WifiOperationData parceledData = WifiOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}