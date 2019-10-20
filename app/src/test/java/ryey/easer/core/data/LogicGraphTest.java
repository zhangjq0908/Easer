/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ryey.easer.commons.C;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LogicGraphTest {

    LogicGraph graph;
    LogicGraph.LogicNode node1, node2, node3, node4;
    static ScriptStructure structure, structure1, structure2, structure3;

    private static LogicGraph edgeToGraph(LogicGraph.LogicNode nodes[], LogicGraph.LogicNode edges[][]) {
        Set<LogicGraph.LogicNode> initials = new HashSet<>();
        Map<String, LogicGraph.LogicNode> idToNode = new HashMap<>();
        Map<LogicGraph.LogicNode, Set<LogicGraph.LogicNode>> edge = new HashMap<>();
        for (LogicGraph.LogicNode node : nodes) {
            idToNode.put(node.id, node);
        }
        for (LogicGraph.LogicNode e[] : edges) {
            if (!edge.containsKey(e[0]))
                edge.put(e[0], new HashSet<>());
            edge.get(e[0]).add(e[1]);
        }
        initials.addAll(Arrays.asList(nodes));
        for (Set<LogicGraph.LogicNode> succs : edge.values()) {
            for (LogicGraph.LogicNode succ : succs) {
                initials.remove(succ);
            }
        }
        return new LogicGraph(initials, idToNode, edge);
    }

    @BeforeClass
    public static void setUpAll() {
        structure = new ScriptStructure(C.VERSION_CREATED_IN_RUNTIME);
        structure.setName("1");
        structure1 = new ScriptStructure(C.VERSION_CREATED_IN_RUNTIME);
        structure1.setName("2");
        structure2 = new ScriptStructure(C.VERSION_CREATED_IN_RUNTIME);
        structure2.setName("3");
        structure3 = new ScriptStructure(C.VERSION_CREATED_IN_RUNTIME);
        structure3.setName("4");
    }

    @Before
    public void setUp() {
        node1 = LogicGraph.LogicNode.createFromScript(structure);
        node2 = LogicGraph.LogicNode.createFromScript(structure1);
        node3 = LogicGraph.LogicNode.createFromScript(structure2);
        node4 = LogicGraph.LogicNode.createFromScript(structure3);
        graph = edgeToGraph(
                new LogicGraph.LogicNode[] {
                        node1, node2, node3, node4
                },
                new LogicGraph.LogicNode[][] {
                        {node1, node2},
                        {node1, node3},
                        {node2, node3},
                        {node3, node4},
                });
    }

    @Test
    public void testInitials() throws Exception {
        Set<LogicGraph.LogicNode> initials = new HashSet<>();
        initials.add(node1);
        assertTrue(graph.initialNodes().containsAll(initials));
        assertTrue(initials.containsAll(graph.initialNodes()));
    }

    @Test
    public void testTopology() throws Exception {
        Set<LogicGraph.LogicNode> nodes = new HashSet<>();
        Set<LogicGraph.LogicNode> succs;

        {
            succs = graph.successors(node1);
            nodes.clear();
            nodes.add(node2);
            nodes.add(node3);
            assertNotNull(succs);
            assertTrue(succs.containsAll(nodes));
            assertTrue(nodes.containsAll(succs));
        }
        {
            succs = graph.successors(node2);
            nodes.clear();
            nodes.add(node3);
            assertNotNull(succs);
            assertTrue(succs.containsAll(nodes));
            assertTrue(nodes.containsAll(succs));
        }
        {
            succs = graph.successors(node3);
            nodes.clear();
            nodes.add(node4);
            assertNotNull(succs);
            assertTrue(succs.containsAll(nodes));
            assertTrue(nodes.containsAll(succs));
        }
        {
            succs = graph.successors(node4);
            assertNull(succs);
        }

    }

}