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

package ryey.easer.core.data;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ryey.easer.commons.C;

import static org.junit.Assert.assertEquals;

public class EventTreeTest {

    EventTree eventTreeRoot, child1, child2, grandchild11;
    static EventStructure structure, structure1, structure2, structure3;

    @BeforeClass
    public static void setUpAll() {
        structure = new EventStructure(C.VERSION_CREATED_IN_RUNTIME);
        structure1 = new EventStructure(C.VERSION_CREATED_IN_RUNTIME);
        structure2 = new EventStructure(C.VERSION_CREATED_IN_RUNTIME);
        structure3 = new EventStructure(C.VERSION_CREATED_IN_RUNTIME);
    }

    @Before
    public void setUp() {
        eventTreeRoot = new EventTree(structure);
        child1 = new EventTree(structure1);
        child2 = new EventTree(structure2);
        grandchild11 = new EventTree(structure3);
    }

    @Test
    public void addAndGetSub() throws Exception {
        assertEquals(eventTreeRoot.getSubs().size(), 0);
        eventTreeRoot.addSub(child1);
        assertEquals(eventTreeRoot.getSubs().size(), 1);
        eventTreeRoot.addSub(child2);
        assertEquals(eventTreeRoot.getSubs().size(), 2);
        child1.addSub(grandchild11);
        assertEquals(eventTreeRoot.getSubs().size(), 2);
    }

}