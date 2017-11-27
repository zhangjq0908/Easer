package ryey.easer.plugins.operation.command;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class CommandOperationDataTest {

    public static CommandOperationData createDummyData() {
        CommandOperationData dummyData = new CommandOperationData();
        dummyData.set("/sdcard/mycmd_file");
        return dummyData;
    }

    @Test
    public void testParcel() {
        CommandOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        CommandOperationData parceledData = CommandOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}