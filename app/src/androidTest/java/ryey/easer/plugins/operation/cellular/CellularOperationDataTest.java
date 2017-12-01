package ryey.easer.plugins.operation.cellular;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class CellularOperationDataTest {

    public static CellularOperationData createDummyData() {
        CellularOperationData dummyData = new CellularOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @Test
    public void testParcel() {
        CellularOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        CellularOperationData parceledData = CellularOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}