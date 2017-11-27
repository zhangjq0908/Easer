package ryey.easer.plugins.operation.wifi;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class WifiOperationDataTest {

    public static WifiOperationData createDummyData() {
        WifiOperationData dummyData = new WifiOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @Test
    public void testParcel() {
        WifiOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        WifiOperationData parceledData = WifiOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}