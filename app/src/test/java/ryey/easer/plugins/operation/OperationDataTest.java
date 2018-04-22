package ryey.easer.plugins.operation;

import org.junit.Test;

import java.lang.reflect.Constructor;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.operationplugin.OperationData;

import static org.junit.Assert.assertEquals;

public class OperationDataTest {

    <T extends OperationData> void testParseAndSerializeMatch(T data0) throws Exception {
        Class<T> klass = (Class<T>) data0.getClass();
        Constructor<T> constructor = klass.getDeclaredConstructor(OperationDataTest.class, String.class, C.Format.class, int.class);
        for (C.Format format : C.Format.values()) {
            String serialized = data0.serialize(format);
            T data1 = constructor.newInstance(this, serialized, format, C.VERSION_CURRENT);
            assertEquals(data0, data1);
        }
    }

    @Test
    public void testParseAndSerializeMatch() throws Exception {
        for (Boolean state : new Boolean[]{true, false}) {
            class IBooleanOperationData extends BooleanOperationData {
                IBooleanOperationData(Boolean state) {
                    super(state);
                }
                IBooleanOperationData(String data, C.Format format, int version) throws IllegalStorageDataException {
                    super(data, format, version);
                }
            }
            BooleanOperationData data0 = new IBooleanOperationData(state);
            testParseAndSerializeMatch(data0);
        }

        for (int[] arr : new int[][]{{0,90,10}, {4,100,20}}) {
            final int ilbound = arr[0];
            final int irbound = arr[1];
            int level = arr[2];
            class IIntegerOperationData extends IntegerOperationData {
                {
                    this.lbound = ilbound;
                    this.rbound = irbound;
                }
                IIntegerOperationData(int level) {
                    super(level);
                }
                IIntegerOperationData(String data, C.Format format, int version) throws IllegalStorageDataException {
                    parse(data, format, version);
                }
            }
            IntegerOperationData data0 = new IIntegerOperationData(level);
            for (C.Format format : C.Format.values()) {
                String serialized = data0.serialize(format);
                IIntegerOperationData data1 = new IIntegerOperationData(serialized, format, C.VERSION_CURRENT);
                assertEquals(data0, data1);
            }
        }

        for (String str : new String[]{"mystr1", "mystr2"}) {
            class IStringOperationData extends StringOperationData {
                IStringOperationData(String data) {
                    super(data);
                }
                IStringOperationData(String data, C.Format format, int version) throws IllegalStorageDataException {
                    parse(data, format, version);
                }
            }
            StringOperationData data0 = new IStringOperationData(str);
            testParseAndSerializeMatch(data0);
        }
    }

}