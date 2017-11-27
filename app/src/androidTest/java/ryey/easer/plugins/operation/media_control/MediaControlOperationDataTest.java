package ryey.easer.plugins.operation.media_control;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class MediaControlOperationDataTest {

    public static MediaControlOperationData createDummyData() {
        MediaControlOperationData dummyData = new MediaControlOperationData();
        dummyData.set(MediaControlOperationData.ControlChoice.next);
        return dummyData;
    }

    @Test
    public void testParcel() {
        MediaControlOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        MediaControlOperationData parceledData = MediaControlOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}