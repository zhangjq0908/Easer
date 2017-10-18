package ryey.easer.core.data;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventTreeTest {

    EventTree eventTreeRoot, child1, child2, grandchild11;
    static EventStructure structure, structure1, structure2, structure3;

    @BeforeClass
    public static void setUpAll() {
        structure = new EventStructure();
        structure1 = new EventStructure();
        structure2 = new EventStructure();
        structure3 = new EventStructure();
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