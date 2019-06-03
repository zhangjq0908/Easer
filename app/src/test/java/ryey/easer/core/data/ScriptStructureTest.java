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

    ScriptStructure scriptStructure, scriptStructure2;
    static final String name = "name to test";
    static final String parentName = "parent name";
    static final String profileName = "profile name";
    static EventStructure scenario;
    static EventData eventData;

    @BeforeClass
    public static void setUpAll() {
        eventData = new WifiUSourceSkill().dataFactory().dummyData();
        scenario = new EventStructure(C.VERSION_CREATED_IN_RUNTIME, "myScenario", eventData);
    }

    @Before
    public void setUp() {
        scriptStructure = new ScriptStructure(C.VERSION_CREATED_IN_RUNTIME);
        scriptStructure2 = new ScriptStructure(C.VERSION_CREATED_IN_RUNTIME);
    }

    @Test
    public void setAndGetName() throws Exception {
        assertEquals(scriptStructure.getName(), null);
        scriptStructure.setName(name);
        assertEquals(scriptStructure.name, name);
        assertEquals(scriptStructure.getName(), name);
    }

    @Test
    public void setAndGetProfileName() throws Exception {
        assertEquals(scriptStructure.getProfileName(), null);
        scriptStructure.setProfileName(profileName);
        assertEquals(scriptStructure.getProfileName(), profileName);
    }

    @Test
    public void setAndGetParentName() throws Exception {
        assertEquals(scriptStructure.getParentName(), null);
        scriptStructure.setParentName(parentName);
        assertEquals(scriptStructure.getParentName(), parentName);
    }

    @Test
    public void getAndSetScenario() throws Exception {
        assertEquals(scriptStructure.getEvent(), null);
        scriptStructure.setEvent(scenario);
        assertEquals(scriptStructure.getEvent(), scenario);
    }

    @Test
    public void setAndTestActive() throws Exception {
        scriptStructure.setActive(true);
        assertTrue(scriptStructure.isActive());
        scriptStructure.setActive(false);
        assertFalse(scriptStructure.isActive());
        scriptStructure.setActive(true);
        assertTrue(scriptStructure.isActive());
    }

    @Test
    public void isValid() throws Exception {
        assertFalse(scriptStructure.isValid());
        scriptStructure.setName(name);
        assertFalse(scriptStructure.isValid());
        scriptStructure.setEvent(scenario);
        assertTrue(scriptStructure.isActive());

        assertFalse(scriptStructure2.isValid());
        scriptStructure2.setEvent(scenario);
        assertFalse(scriptStructure2.isValid());
        scriptStructure2.setName(name);
        assertTrue(scriptStructure2.isActive());
    }
}