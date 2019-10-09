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
import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.skills.usource.wifi.WifiUSourceSkill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ScriptStructureTest {

    static final String name = "name to test";
    static final String parentName = "parent name";
    static final String profileName = "profile name";
    static EventStructure eventStructure;
    static EventData eventData;

    @BeforeClass
    public static void setUpAll() {
        eventData = new WifiUSourceSkill().dataFactory().dummyData();
        eventStructure = new EventStructure(C.VERSION_CREATED_IN_RUNTIME, "myScenario", eventData);
    }

    @Test
    public void testBuilder() throws Exception {
        assertTrue(canBuild(new ScriptStructure.Builder(C.VERSION_CREATED_IN_RUNTIME)
                .setName("1")
                .setEvent(eventStructure)
        ));

        ScriptStructure.Builder builder = new ScriptStructure.Builder(C.VERSION_CREATED_IN_RUNTIME);
        assertFalse(canBuild(builder));
        builder.setName(name);
        assertFalse(canBuild(builder));
        builder.setProfileName(profileName);
        assertFalse(canBuild(builder));
        builder.addPredecessor(parentName);
        assertFalse(canBuild(builder));
        builder.setEvent(eventStructure);
        assertTrue(canBuild(builder));
    }

    @Test
    public void testOrderIndependent() throws Exception {
        ScriptStructure.Builder b1 = new ScriptStructure.Builder(C.VERSION_CREATED_IN_RUNTIME)
                .setName(name)
                .setProfileName(profileName)
                .addPredecessor(parentName)
                .setEvent(eventStructure);
        ScriptStructure.Builder b2 = new ScriptStructure.Builder(C.VERSION_CREATED_IN_RUNTIME)
                .addPredecessor(parentName)
                .setProfileName(profileName)
                .setEvent(eventStructure)
                .setName(name);
        assertEquals(b1.build(), b2.build());
    }

    private static boolean canBuild(ScriptStructure.Builder builder) {
        try {
            builder.build();
            return true;
        } catch (BuilderInfoClashedException e) {
            return false;
        }
    }
}