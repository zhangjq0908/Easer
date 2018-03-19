package ryey.easer.plugins.operation.ringer_mode;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.TestHelper;

import static org.junit.Assert.assertEquals;

public class RingerModeOperationDataTest {

    @Test
    public void testParcel() {
        RingerModeOperationData dummyData = new RingerModeOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        RingerModeOperationData parceledData = RingerModeOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}