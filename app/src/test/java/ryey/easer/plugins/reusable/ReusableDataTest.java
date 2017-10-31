package ryey.easer.plugins.reusable;

import org.junit.Test;

import ryey.easer.plugins.operation.BooleanOperationData;
import ryey.easer.plugins.operation.IntegerOperationData;
import ryey.easer.plugins.operation.StringOperationData;

import static org.junit.Assert.*;

public class ReusableDataTest {

    @Test
    public void testSetAndGet() throws Exception {
        for (Boolean state : new Boolean[]{true, false}) {
            class IBooleanOperationData extends BooleanOperationData {}
            BooleanOperationData data0 = new IBooleanOperationData();
            data0.set(state);
            assertEquals(data0.get(), state);
        }

        for (int level : new int[]{-100, 10, 101}) {
            class IIntegerOperationData extends IntegerOperationData {}
            IntegerOperationData data0 = new IIntegerOperationData();
            data0.set(level);
            assertEquals(data0.get(), level);
        }

        for (String str : new String[]{"mystr1", "mystr2"}) {
            class IStringOperationData extends StringOperationData {}
            StringOperationData data0 = new IStringOperationData();
            data0.set(str);
            assertEquals(data0.get(), str);
        }
    }

    @Test
    public void testIsValid() throws Exception {
        for (Boolean state : new Boolean[]{true, false, null}) {
            class IBooleanOperationData extends BooleanOperationData {}
            BooleanOperationData data0 = new IBooleanOperationData();
            if (state != null)
                data0.set(state);
            assertTrue(data0.isValid() == (state != null));
        }

        for (final int ilbound : new int[]{0, 4}) {
            for (final int irbound : new int[]{90, 100}) {
                for (int level : new int[]{-100, ilbound, 10, irbound, 101}) {
                    class IIntegerOperationData extends IntegerOperationData {
                        IIntegerOperationData(int lbound, int rbound) {
                            this.lbound = lbound;
                            this.rbound = rbound;
                        }
                    }
                    IntegerOperationData data0 = new IIntegerOperationData(ilbound, irbound);
                    data0.set(level);
                    if (level < ilbound)
                        assertFalse(data0.isValid());
                    if (level > irbound)
                        assertFalse(data0.isValid());
                    if (level >= ilbound && level <= irbound)
                        assertTrue(data0.isValid());
                }
            }
        }
    }
}