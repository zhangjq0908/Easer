package ryey.easer.plugins.operation.command;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class CommandOperationDataTest {

    @Test
    public void testParcel() {
        CommandOperationData dummyData = new CommandOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        CommandOperationData parceledData = CommandOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}