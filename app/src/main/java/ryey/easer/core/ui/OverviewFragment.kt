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

package ryey.easer.core.ui

import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.blox.graphview.*
import de.blox.graphview.tree.BuchheimWalkerAlgorithm
import de.blox.graphview.tree.BuchheimWalkerConfiguration
import ryey.easer.R
import ryey.easer.core.data.Checkpoint
import ryey.easer.core.data.ScriptTree
import ryey.easer.core.data.storage.ScriptDataStorage


class OverviewFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.title_overview)

        val view: View = inflater.inflate(R.layout.fragment_overview, container, false)

        val graphView = view.findViewById<GraphView>(R.id.graph)

        val scriptDataStorage = ScriptDataStorage(context)
        val scriptTrees = scriptDataStorage.scriptTrees

        val virtualRoot = Node(ServiceStart())

        val graph = Graph()
        graph.addNode(virtualRoot)

        for (scriptTree in scriptTrees) {
            addToGraph(scriptTree, graph, virtualRoot)
        }

        val adapter = object : BaseGraphAdapter<ViewHolder>(graph) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.node_script, parent, false)
                return SimpleViewHolder(v)
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, data: Any, position: Int) {
                if (data is Checkpoint) {
                    (viewHolder as SimpleViewHolder).tvName.text = data.name
                    @ColorInt var color: Int
                    color = if (data.valid) {
                        R.color.nodeText_scriptValid
                    } else {
                        R.color.nodeText_scriptInvalid
                    }
                    viewHolder.tvName.setTextColor(resources.getColor(color))

                    color = if (data.enabled) {
                        R.color.nodeBackground_scriptActive
                    } else {
                        R.color.nodeBackground_scriptInactive
                    }
                    viewHolder.setBackgroundColor(resources.getColor(color))

                    viewHolder.layoutScriptExtra.visibility = View.VISIBLE
                    viewHolder.tvType.text = getString(if (data.isCondition) {
                        R.string.title_condition
                    } else {
                        R.string.title_event
                    })
                } else {
                    viewHolder as SimpleViewHolder
                    viewHolder.layoutScriptExtra.visibility = View.GONE
                    if (data is ProfileInfo) {
                        viewHolder.tvName.text = data.name
                        viewHolder.setBackgroundColor(resources.getColor(R.color.nodeBackground_Profile))
                    } else if (data is ServiceStart) {
                        viewHolder.tvName.text = getString(data.nameId())
                        viewHolder.setBackgroundColor(resources.getColor(R.color.nodeBackground_VirtualRoot))
                    }
                }
            }
        }
        graphView.adapter = adapter

        // set the algorithm here
        val configuration = BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(300)
                .setSubtreeSeparation(300)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build()
        adapter.algorithm = BuchheimWalkerAlgorithm(configuration)

        return view
    }

    companion object {
        fun addToGraph(scriptTree: ScriptTree, graph: Graph, parent: Node) {
            val node = Node(Checkpoint.fromScript(scriptTree.data))
            graph.addNode(node)
            graph.addEdge(parent, node)
            if (scriptTree.profile != null) {
                val profileNode = Node(ProfileInfo(scriptTree.profile))
                graph.addNode(profileNode)
                graph.addEdge(node, profileNode)
            }
            for (subTree in scriptTree.subs) {
                addToGraph(subTree, graph, node)
            }
        }
    }

    class ServiceStart {
        @StringRes
        fun nameId(): Int {
            return R.string.title_service_start
        }
    }

    class ProfileInfo internal constructor(val name: String)

    class SimpleViewHolder internal constructor(itemView: View) : ViewHolder(itemView) {
        internal val tvName: TextView = itemView.findViewById(R.id.name)
        internal val layoutScriptExtra: ViewGroup = itemView.findViewById(R.id.extra_script_info)
        internal val tvType: TextView = itemView.findViewById(R.id.tv_type)

        fun setBackgroundColor(@ColorInt color: Int) {
            itemView.setBackgroundColor(color)
        }
    }
}