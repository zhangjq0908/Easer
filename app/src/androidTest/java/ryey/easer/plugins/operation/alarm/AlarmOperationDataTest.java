package ryey.easer.plugins.operation.alarm;

import android.os.Parcel;

import org.junit.Test;

import java.util.Calendar;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class AlarmOperationDataTest {

    public static AlarmOperationData createDummyData() {
        AlarmOperationData dummyData = new AlarmOperationData();
        AlarmOperationData.AlarmData alarmData = new AlarmOperationData.AlarmData();
        alarmData.time = Calendar.getInstance();
        alarmData.message = "my message";
        alarmData.absolute = false;
        dummyData.set(alarmData);
        return dummyData;
    }

    @Test
    public void testParcel() {
        AlarmOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        AlarmOperationData parceledData = AlarmOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}