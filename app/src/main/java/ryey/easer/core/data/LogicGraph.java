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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class LogicGraph {

    public static LogicGraph createFromScriptList(Collection<ScriptStructure> scripts) {
        Set<LogicNode> initials = new ArraySet<>();
        Map<String, LogicNode> idToNode = new HashMap<>();
        Map<LogicNode, Set<LogicNode>> edge = new HashMap<>();
        for (ScriptStructure script : scripts) {
            LogicNode node = LogicNode.createFromScript(script);
            idToNode.put(node.id, node);
        }
        for (ScriptStructure script : scripts) {
            if (script.getParentName() == null) {
                initials.add(idToNode.get(script.getName()));
            } else {
                LogicNode predecessor = Objects.requireNonNull(idToNode.get(script.getParentName()));
                Set<LogicNode> set = edge.get(predecessor);
                if (set == null)
                    set = new ArraySet<>();
                set.add(idToNode.get(script.getName()));
                edge.put(predecessor, set);
            }
        }
        return new LogicGraph(initials, idToNode, edge);
    }

    @NonNull private final Set<LogicNode> initials;
    @NonNull private final Map<String, LogicNode> idToNode;
    @NonNull private final Map<LogicNode, Set<LogicNode>> edge;

    LogicGraph(@NonNull Set<LogicNode> initials, @NonNull Map<String, LogicNode> idToNode, @NonNull Map<LogicNode, Set<LogicNode>> edge) {
        this.initials = initials;
        this.idToNode = idToNode;
        this.edge = edge;
    }

    @NonNull public Set<LogicNode> initialNodes() {
        return initials;
    }

    @Nullable public LogicNode getNode(@NonNull String id) {
        return idToNode.get(id);
    }

    @Nullable public Set<LogicNode> successors(@NonNull LogicNode node) {
        return edge.get(node);
    }

    /**
     * "M" stands for "must"
     * @param node
     * @return
     */
    @NonNull public Set<LogicNode> successorsM(@NonNull LogicNode node) {
        Set<LogicNode> succs = successors(node);
        if (succs == null)
            return Collections.emptySet();
        return succs;
    }

    public static class LogicNode {

        public static LogicNode createFromScript(ScriptStructure scriptStructure) {
            return new LogicNode(scriptStructure.getName(), scriptStructure.isActive(), scriptStructure);
        }

        @NonNull
        public final String id;
        public final boolean active;
        @NonNull
        public final ScriptStructure script;

        public LogicNode(@NonNull String id, boolean active, @NonNull ScriptStructure script) {
            this.id = id;
            this.active = active;
            this.script = script;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof LogicNode))
                return false;
            return id.equals(((LogicNode) obj).id);
        }
    }
}
