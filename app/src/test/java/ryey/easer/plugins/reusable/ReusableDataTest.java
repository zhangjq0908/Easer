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

package ryey.easer.plugins.reusable;

import android.support.annotation.NonNull;

import org.junit.Test;

import ryey.easer.commons.local_plugin.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_plugin.operationplugin.OperationData;
import ryey.easer.plugins.operation.BooleanOperationData;
import ryey.easer.plugins.operation.IntegerOperationData;
import ryey.easer.plugins.operation.StringOperationData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ReusableDataTest {

    @Test
    public void testSetAndGet() throws Exception {
        for (Boolean state : new Boolean[]{true, false}) {
            class IBooleanOperationData extends BooleanOperationData {}
            BooleanOperationData data0 = new IBooleanOperationData();
            data0.set(state);
            assertEquals(data0.state, state);
        }

        for (int level : new int[]{-100, 10, 101}) {
            class IIntegerOperationData extends IntegerOperationData {}
            IntegerOperationData data0 = new IIntegerOperationData();
            data0.set(level);
            assertEquals((int) data0.get(), level);
        }

        for (String str : new String[]{"mystr1", "mystr2"}) {
            class IStringOperationData extends StringOperationData {
                @NonNull
                @Override
                public OperationData applyDynamics(SolidDynamicsAssignment dynamicsAssignment) {
                    fail();
                    return null;
                }
            }
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