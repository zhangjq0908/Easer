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