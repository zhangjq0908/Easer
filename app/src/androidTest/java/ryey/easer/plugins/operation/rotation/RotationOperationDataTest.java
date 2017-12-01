package ryey.easer.plugins.operation.rotation;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class RotationOperationDataTest {

    public static RotationOperationData createDummyData() {
        RotationOperationData dummyData = new RotationOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @Test
    public void testParcel() {
        RotationOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        RotationOperationData parceledData = RotationOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}