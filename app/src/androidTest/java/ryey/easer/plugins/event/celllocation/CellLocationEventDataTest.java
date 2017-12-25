package ryey.easer.plugins.event.celllocation;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class CellLocationEventDataTest {

    @Test
    public void testParcel() {
        CellLocationEventData dummyData = (CellLocationEventData) new CellLocationEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        CellLocationEventData parceledData = CellLocationEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}