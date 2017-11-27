package ryey.easer.plugins.operation.hotspot;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class HotspotOperationDataTest {

    public static HotspotOperationData createDummyData() {
        HotspotOperationData dummyData = new HotspotOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @Test
    public void testParcel() {
        HotspotOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        HotspotOperationData parceledData = HotspotOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}