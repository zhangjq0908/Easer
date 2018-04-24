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
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.plugins.event.wifi.WifiEventPlugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventStructureTest {

    EventStructure eventStructure, eventStructure2;
    static final String name = "name to test";
    static final String parentName = "parent name";
    static final String profileName = "profile name";
    static ScenarioStructure scenario;
    static EventData eventData;

    @BeforeClass
    public static void setUpAll() {
        eventData = new WifiEventPlugin().dataFactory().dummyData();
        scenario = new ScenarioStructure(C.VERSION_CREATED_IN_RUNTIME, "myScenario", eventData);
    }

    @Before
    public void setUp() {
        eventStructure = new EventStructure(C.VERSION_CREATED_IN_RUNTIME);
        eventStructure2 = new EventStructure(C.VERSION_CREATED_IN_RUNTIME);
    }

    @Test
    public void setAndGetName() throws Exception {
        assertEquals(eventStructure.getName(), null);
        eventStructure.setName(name);
        assertEquals(eventStructure.name, name);
        assertEquals(eventStructure.getName(), name);
    }

    @Test
    public void setAndGetProfileName() throws Exception {
        assertEquals(eventStructure.getProfileName(), null);
        eventStructure.setProfileName(profileName);
        assertEquals(eventStructure.getProfileName(), profileName);
    }

    @Test
    public void setAndGetParentName() throws Exception {
        assertEquals(eventStructure.getParentName(), null);
        eventStructure.setParentName(parentName);
        assertEquals(eventStructure.getParentName(), parentName);
    }

    @Test
    public void getAndSetScenario() throws Exception {
        assertEquals(eventStructure.getScenario(), null);
        eventStructure.setScenario(scenario);
        assertEquals(eventStructure.getScenario(), scenario);
    }

    @Test
    public void setAndTestActive() throws Exception {
        eventStructure.setActive(true);
        assertTrue(eventStructure.isActive());
        eventStructure.setActive(false);
        assertFalse(eventStructure.isActive());
        eventStructure.setActive(true);
        assertTrue(eventStructure.isActive());
    }

    @Test
    public void isValid() throws Exception {
        assertFalse(eventStructure.isValid());
        eventStructure.setName(name);
        assertFalse(eventStructure.isValid());
        eventStructure.setScenario(scenario);
        assertTrue(eventStructure.isActive());

        assertFalse(eventStructure2.isValid());
        eventStructure2.setScenario(scenario);
        assertFalse(eventStructure2.isValid());
        eventStructure2.setName(name);
        assertTrue(eventStructure2.isActive());
    }
}