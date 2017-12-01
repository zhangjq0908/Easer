package ryey.easer.plugins.event.celllocation;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class CellLocationEventDataTest {

    public static CellLocationEventData createDummyData() {
        CellLocationEventData dummyData = new CellLocationEventData();
        dummyData.set(new String[]{"1-2", "2-3"});
        return dummyData;
    }

    @Test
    public void testParcel() {
        CellLocationEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        CellLocationEventData parceledData = CellLocationEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}