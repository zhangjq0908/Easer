package ryey.easer.plugins.operation.brightness;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class BrightnessOperationDataTest {

    public static BrightnessOperationData createDummyData() {
        BrightnessOperationData dummyData = new BrightnessOperationData();
        dummyData.set(3);
        return dummyData;
    }

    @Test
    public void testParcel() {
        BrightnessOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BrightnessOperationData parceledData = BrightnessOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}