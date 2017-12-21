package ryey.easer.plugins.operation.cellular;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class CellularOperationDataTest {

    @Test
    public void testParcel() {
        CellularOperationData dummyData = (CellularOperationData) new CellularOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        CellularOperationData parceledData = CellularOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}