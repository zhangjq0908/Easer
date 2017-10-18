package ryey.easer.core.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventStructureTest {
    EventStructure eventStructure = new EventStructure();
    String name = "name to test";
    String parentName = "parent name";
    String profileName = "profile name";

    @Test
    public void constructor() {
        EventStructure structure1 = new EventStructure();
        EventStructure structure2 = new EventStructure(name);
        assertEquals(structure1.getName(), null);
        assertEquals(structure2.getName(), name);
        assertEquals(structure1.getEventData(), structure2.getEventData());
        assertEquals(structure1.getParentName(), structure2.getParentName());
        assertEquals(structure1.getProfileName(), structure2.getProfileName());
    }

    @Test
    public void setAndGetName() throws Exception {
        eventStructure = new EventStructure();
        eventStructure.setName(name);
        assertEquals(eventStructure.getName(), name);
    }

    @Test
    public void setAndGetProfileName() throws Exception {
        eventStructure.setProfileName(profileName);
        assertEquals(eventStructure.getProfileName(), profileName);
    }

    @Test
    public void setAndGetParentName() throws Exception {
        eventStructure.setParentName(parentName);
        assertEquals(eventStructure.getParentName(), parentName);
    }

}