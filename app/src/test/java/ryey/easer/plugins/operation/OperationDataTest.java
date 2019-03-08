/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.plugins.operation;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.lang.reflect.Constructor;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.local_plugin.IllegalStorageDataException;
import ryey.easer.commons.local_plugin.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_plugin.operationplugin.OperationData;
import ryey.easer.plugin.PluginDataFormat;

import static org.junit.Assert.assertEquals;

public class OperationDataTest {

    <T extends OperationData> void testParseAndSerializeMatch(T data0) throws Exception {
        Class<T> klass = (Class<T>) data0.getClass();
        Constructor<T> constructor = klass.getDeclaredConstructor(OperationDataTest.class, String.class, PluginDataFormat.class, int.class);
        for (PluginDataFormat format : PluginDataFormat.values()) {
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
                IBooleanOperationData(String data, PluginDataFormat format, int version) throws IllegalStorageDataException {
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
                IIntegerOperationData(String data, PluginDataFormat format, int version) throws IllegalStorageDataException {
                    parse(data, format, version);
                }
            }
            IntegerOperationData data0 = new IIntegerOperationData(level);
            for (PluginDataFormat format : PluginDataFormat.values()) {
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
                IStringOperationData(String data, PluginDataFormat format, int version) throws IllegalStorageDataException {
                    parse(data, format, version);
                }
                @NonNull
                @Override
                public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
                    return new IStringOperationData(Utils.applyDynamics(this.text, dynamicsAssignment));
                }
            }
            StringOperationData data0 = new IStringOperationData(str);
            testParseAndSerializeMatch(data0);
        }
    }

}