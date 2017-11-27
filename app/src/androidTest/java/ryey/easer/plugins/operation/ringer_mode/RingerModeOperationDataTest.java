package ryey.easer.plugins.operation.ringer_mode;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class RingerModeOperationDataTest {

    public static RingerModeOperationData createDummyData() {
        RingerModeOperationData dummyData = new RingerModeOperationData();
        dummyData.set(1);
        return dummyData;
    }

    @Test
    public void testParcel() {
        RingerModeOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        RingerModeOperationData parceledData = RingerModeOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}