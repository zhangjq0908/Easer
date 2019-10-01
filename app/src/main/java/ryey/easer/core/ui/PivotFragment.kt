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

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import de.blox.graphview.*
import de.blox.graphview.layered.SugiyamaAlgorithm
import de.blox.graphview.layered.SugiyamaConfiguration
import ryey.easer.R
import ryey.easer.core.EHService
import ryey.easer.core.Lotus
import ryey.easer.core.data.LogicGraph
import ryey.easer.core.data.ScriptStructure
import ryey.easer.core.data.storage.ScriptDataStorage
import java.util.*
import kotlin.collections.HashMap


class PivotFragment: Fragment() {

    private val lotusStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val satisfied = intent!!.getBooleanExtra(Lotus.EXTRA_SATISFACTION, false)
            val id = intent.getStringExtra(Lotus.EXTRA_SCRIPT_ID)
            updateNodeStatus(id, satisfied)
        }
    }

    var graph: Graph? = null
    private var checkpointNodeMap: Map<String, Node>? = null

    var adapter: BaseGraphAdapter<ViewHolder>? = null

    private val serviceConnection = object: ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            binder = null
            redrawGraph()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = service as EHService.EHBinder
            redrawGraph()
        }
    }
    private var binder: EHService.EHBinder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.title_pivot)

        val view: View = inflater.inflate(R.layout.fragment_pivot, container, false)

        val graphView = view.findViewById<GraphView>(R.id.graph)

        recreateGraph()

        adapter = object : BaseGraphAdapter<ViewHolder>(graph!!) {

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
                    if (data.satisfied == null) {
                        viewHolder.ivNodeStatus.visibility = View.INVISIBLE
                    } else {
                        viewHolder.ivNodeStatus.visibility = View.VISIBLE
                        val drawable = context!!.resources.getDrawable(
                                if (data.satisfied == true) {
                                    R.drawable.ind_node_positive
                                } else {
                                    R.drawable.ind_node_negative
                                })
                        viewHolder.ivNodeStatus.setImageDrawable(drawable)
                    }

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

        val configurationBuilder = SugiyamaConfiguration.Builder().setLevelSeparation(300).setNodeSeparation(100)
        val algorithm = SugiyamaAlgorithm(SugiyamaConfiguration(configurationBuilder))
        adapter!!.algorithm = algorithm

        return view
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(context!!).registerReceiver(lotusStatusReceiver, lotusStateIntentFilter)
        context!!.bindService(Intent(context, EHService::class.java), serviceConnection, 0)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(lotusStatusReceiver)
        context!!.unbindService(serviceConnection)
    }

    fun updateNodeStatus(id: String, satisfied: Boolean) {
        val node = checkpointNodeMap?.get(id)
        if (node != null) {
            updateNodeStatus(node, satisfied)
        }
    }

    private fun updateNodeStatus(node: Node, satisfied: Boolean?) {
        redrawGraph()
    }

    private fun redrawGraph() {
        recreateGraph()
        adapter!!.graph = graph
        adapter!!.notifyInvalidated()
    }

    private fun recreateGraph() {
        val ret = if (binder == null) {
            createBasicGraph()
        } else {
            createAdvancedGraph()
        }
        graph = ret.first
        checkpointNodeMap = ret.second
    }

    private fun createBasicGraph(): Pair<Graph, Map<String, Node>> {
        val scriptDataStorage = ScriptDataStorage(context)

        return convertToGraph(scriptDataStorage.logicGraph, null)
    }

    private fun createAdvancedGraph(): Pair<Graph, Map<String, Node>> {
        val statusMap: Map<String, Boolean> = binder!!.lotusStatusMap()

        val scriptDataStorage = ScriptDataStorage(context)

        return convertToGraph(scriptDataStorage.logicGraph, statusMap)
    }

    companion object {
        val lotusStateIntentFilter = IntentFilter(Lotus.ACTION_LOTUS_SATISFACTION_CHANGED)

        fun convertToGraph(logicGraph: LogicGraph, statusMap: Map<String, Boolean>?): Pair<Graph, Map<String, Node>> {
            val graph = Graph()
            val nodeMap = HashMap<String, Node>()

            val queue = LinkedList<LogicGraph.LogicNode>()

            fun newNode(logicNode: LogicGraph.LogicNode): Node {
                val checkpoint = Checkpoint.fromScript(logicNode.script)
                checkpoint.satisfied = statusMap?.get(checkpoint.name)
                val node = Node(checkpoint)
                graph.addNode(node)
                queue.add(logicNode)
                nodeMap[logicNode.id] = node
                return node
            }

            for (logicNode in logicGraph.initialNodes()) {
                newNode(logicNode)
            }

            while (queue.isNotEmpty()) {
                val logicNode = queue.poll()
                val node = nodeMap[logicNode.id]!!
                logicNode.script.profileName?.let {
                    val profileNode = Node(ProfileInfo(it))
                    graph.addNode(profileNode)
                    graph.addEdge(node, profileNode)
                }
                logicGraph.successors(logicNode)?.let {
                    for (successor in it) {
                        var node2 = nodeMap[successor.id]
                        if (node2 == null) {
                            node2 = newNode(successor)
                        }
                        graph.addEdge(node, node2)
                    }
                }
            }
            return Pair(graph, nodeMap)
        }
    }

    /**
     * This class represents a CheckPoint in the background state (i.e. the collection of all Scripts and their dependencies)
     *
     * Currently it is a projection from ScriptStructure
     * TODO: Use this class as the base of LogicNode instead of Script in everywhere (EHService, Lotus, PivotFragment, TODO)
     * TODO: Replace most of Script's role
     *
     * TODO: Convert relevant interfaces to Kotlin so that this class can be written in kotlin
     */
    class Checkpoint(val name: String, val enabled: Boolean, val valid: Boolean, val isCondition: Boolean) {

        var satisfied: Boolean? = null

        override fun equals(other: Any?): Boolean {
            if (other === this)
                return true
            if (other !is Checkpoint)
                return false
            return name == other.name
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }

        companion object {

            fun fromScript(scriptStructure: ScriptStructure): Checkpoint {
                val name = scriptStructure.name
                val enabled = scriptStructure.isActive
                val valid = scriptStructure.isValid
                return Checkpoint(name, enabled, valid, scriptStructure.isCondition)
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
        internal val ivNodeStatus: ImageView = itemView.findViewById(R.id.iv_node_status)

        fun setBackgroundColor(@ColorInt color: Int) {
            itemView.setBackgroundColor(color)
        }
    }
}
