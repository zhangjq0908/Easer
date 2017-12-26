package ryey.easer.plugins.operation.brightness;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class BrightnessOperationDataTest {

    @Test
    public void testParcel() {
        BrightnessOperationData dummyData = new BrightnessOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BrightnessOperationData parceledData = BrightnessOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}