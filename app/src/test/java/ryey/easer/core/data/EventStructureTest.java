package ryey.easer.core.data;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.wifi.WifiEventData;
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
        scenario = new ScenarioStructure("myScenario", eventData);
    }

    @Before
    public void setUp() {
        eventStructure = new EventStructure();
        eventStructure2 = new EventStructure();
    }

    @Test
    public void constructor() {
        EventStructure structure1 = new EventStructure();
        EventStructure structure2 = new EventStructure(name);
        assertEquals(structure1.getName(), null);
        assertEquals(structure2.getName(), name);
        assertEquals(structure1.getScenario(), structure2.getScenario());
        assertEquals(structure1.getParentName(), structure2.getParentName());
        assertEquals(structure1.getProfileName(), structure2.getProfileName());
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