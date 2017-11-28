package ryey.easer.plugins.operation;

import org.junit.Test;

import ryey.easer.commons.C;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

import static org.junit.Assert.assertEquals;

public class OperationDataTest {

    static void testParseAndSerializeMatch(OperationData data0, OperationData data1) throws Exception {
        for (C.Format format : C.Format.values()) {
            String serialized = data0.serialize(format);
            data1.parse(serialized, format, C.VERSION_CURRENT);
            assertEquals(data0, data1);
        }
    }

    @Test
    public void testParseAndSerializeMatch() throws Exception {
        for (Boolean state : new Boolean[]{true, false}) {
            class IBooleanOperationData extends BooleanOperationData {}
            BooleanOperationData data0 = new IBooleanOperationData();
            BooleanOperationData data1 = new IBooleanOperationData();
            data0.set(state);
            testParseAndSerializeMatch(data0, data1);
        }

        for (int[] arr : new int[][]{{0,90,10}, {4,100,20}}) {
            int ilbound = arr[0];
            int irbound = arr[1];
            int level = arr[2];
            class IIntegerOperationData extends IntegerOperationData {
                IIntegerOperationData(int lbound, int rbound) {
                    this.lbound = lbound;
                    this.rbound = rbound;
                }
            }
            IntegerOperationData data0 = new IIntegerOperationData(ilbound, irbound);
            IntegerOperationData data1 = new IIntegerOperationData(ilbound, irbound);
            data0.set(level);
            testParseAndSerializeMatch(data0, data1);
        }

        for (String str : new String[]{"mystr1", "mystr2"}) {
            class IStringOperationData extends StringOperationData {}
            StringOperationData data0 = new IStringOperationData();
            StringOperationData data1 = new IStringOperationData();
            data0.set(str);
            testParseAndSerializeMatch(data0, data1);
        }
    }

}