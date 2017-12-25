package ryey.easer.plugins.operation.media_control;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class MediaControlOperationDataTest {

    @Test
    public void testParcel() {
        MediaControlOperationData dummyData = new MediaControlOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        MediaControlOperationData parceledData = MediaControlOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}