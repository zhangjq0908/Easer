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

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import de.blox.graphview.*
import de.blox.graphview.layered.SugiyamaAlgorithm
import de.blox.graphview.layered.SugiyamaConfiguration
import kotlinx.android.synthetic.main.fragment_pivot.*
import ryey.easer.R
import ryey.easer.core.AsyncHelper
import ryey.easer.core.EHService
import ryey.easer.core.Lotus
import ryey.easer.core.data.LogicGraph
import ryey.easer.core.data.ScriptStructure
import ryey.easer.core.data.storage.ScriptDataStorage
import ryey.easer.core.ui.data.EditDataProto
import ryey.easer.core.ui.data.profile.EditProfileActivity
import ryey.easer.core.ui.data.script.EditScriptActivity
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashMap

class PivotFragment: Fragment() {

    private val lotusStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val satisfied = intent!!.getBooleanExtra(Lotus.EXTRA_SATISFACTION, false)
            val id = intent.getStringExtra(Lotus.EXTRA_SCRIPT_ID)!!
            updateNodeStatus(id, satisfied)
        }
    }

    val onViewJob = AsyncHelper.DelayedWhenSatisfied()

    private lateinit var checkpointNodeMap: Map<String, Node>
    private lateinit var adapter: BaseGraphAdapter<ViewHolder>

    private var loadProfileJobWrapper = AsyncHelper.DelayedLoadProfileJobsWrapper()
    private val serviceConnection = object: ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            binder = null
            onViewJob.doAfter {
                redrawGraph()
                null
            }
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = service as EHService.EHBinder
            onViewJob.doAfter {
                redrawGraph()
                null
            }
        }
    }
    private var binder: EHService.EHBinder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.title_pivot)

        val view: View = inflater.inflate(R.layout.fragment_pivot, container, false)

        val graphView = view.findViewById<GraphView>(R.id.graph)

        val ret = recreateGraph()
        checkpointNodeMap = ret.second
        adapter = GraphAdapter(ret.first)
        graphView.adapter = adapter

        val configurationBuilder = SugiyamaConfiguration.Builder().setLevelSeparation(300).setNodeSeparation(100)
        val algorithm = SugiyamaAlgorithm(SugiyamaConfiguration(configurationBuilder))
        adapter.algorithm = algorithm

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewJob.onSatisfied()

        fab.setOnClickListener {
            EditDataProto.addScript(this, Intent(activity, EditScriptActivity::class.java), REQUEST_CODE, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onViewJob.onUnsatisfied()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(context!!).registerReceiver(lotusStatusReceiver, lotusStateIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(lotusStatusReceiver)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.bindService(Intent(context, EHService::class.java), serviceConnection, 0)
        loadProfileJobWrapper.bindService(context)
    }

    override fun onDetach() {
        super.onDetach()
        loadProfileJobWrapper.unbindService(context!!)
        context!!.unbindService(serviceConnection)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val name: String? = {
            val tvName: TextView? = SimpleViewHolder.currentView?.get()?.findViewById(R.id.name)
            tvName?.text.toString()
        }()
        when (item.itemId) {
            R.id.action_add_successor_script -> {
                EditDataProto.addScript(this, Intent(activity, EditScriptActivity::class.java), REQUEST_CODE, name!!)
                return true
            }
            R.id.action_edit_script -> {
                EditDataProto.edit(this, Intent(activity, EditScriptActivity::class.java), REQUEST_CODE, name!!)
                return true
            }
            R.id.action_delete_script -> {
                EditDataProto.delete(this, Intent(activity, EditScriptActivity::class.java), REQUEST_CODE, name!!)
                return true
            }
            R.id.action_edit_profile -> {
                EditDataProto.edit(this, Intent(activity, EditProfileActivity::class.java), REQUEST_CODE, name!!)
                return true
            }
            R.id.action_trigger_profile -> {
                loadProfileJobWrapper.triggerProfile(name!!)
                return true
            }
            else -> return false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            redrawGraph()
        }
    }

    fun updateNodeStatus(id: String, satisfied: Boolean) {
        fun updateNodeStatus(node: Node, satisfied: Boolean?) {
            //TODO: partial update (requires upstream library support)
            redrawGraph()
        }
        onViewJob.doAfter {
            val node = checkpointNodeMap[id]
            if (node != null) {
                updateNodeStatus(node, satisfied)
            }
            null
        }
    }

    private fun redrawGraph() {
        val ret = recreateGraph()
        val graph = ret.first
        checkpointNodeMap = ret.second
        adapter.graph = graph
        adapter.notifyInvalidated()
    }

    private fun recreateGraph(): Pair<Graph, Map<String, Node>> {
        return if (binder == null) {
            createBasicGraph()
        } else {
            createAdvancedGraph()
        }
    }

    private fun createBasicGraph(): Pair<Graph, Map<String, Node>> {
        val scriptDataStorage = ScriptDataStorage(context!!)

        return convertToGraph(scriptDataStorage.logicGraph, null)
    }

    private fun createAdvancedGraph(): Pair<Graph, Map<String, Node>> {
        val statusMap: Map<String, Boolean> = binder!!.lotusStatusMap()

        val scriptDataStorage = ScriptDataStorage(context!!)

        return convertToGraph(scriptDataStorage.logicGraph, statusMap)
    }

    companion object {
        val lotusStateIntentFilter = IntentFilter(Lotus.ACTION_LOTUS_SATISFACTION_CHANGED)

        const val REQUEST_CODE = 10

        fun convertToGraph(logicGraph: LogicGraph, statusMap: Map<String, Boolean>?): Pair<Graph, Map<String, Node>> {
            val graph = Graph()
            val nodeMap = HashMap<String, Node>()

            val queue = LinkedList<LogicGraph.LogicNode>()

            fun newNode(logicNode: LogicGraph.LogicNode): Node {
                val checkpoint = Checkpoint.fromScript(logicNode.script, (logicGraph.successors(logicNode)?.size ?: 0) > 0)
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
                val logicNode = queue.poll()!!
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
    class Checkpoint(val name: String, val enabled: Boolean, val valid: Boolean, val isCondition: Boolean, val hasSuccessors: Boolean) {

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

            fun fromScript(scriptStructure: ScriptStructure, hasSuccessors: Boolean): Checkpoint {
                val name = scriptStructure.name
                val enabled = scriptStructure.isActive
                val valid = scriptStructure.isValid
                return Checkpoint(name, enabled, valid, scriptStructure.isCondition, hasSuccessors)
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

        fun setScriptContextMenu(inflater: MenuInflater, hasSuccessors: Boolean) {
            itemView.setOnCreateContextMenuListener { menu, v, menuInfo ->
                currentView = WeakReference(itemView)
                inflater.inflate(R.menu.pivot_context_script, menu)
                menu.findItem(R.id.action_delete_script).isVisible = !hasSuccessors
            }
        }

        fun setProfileContextMenu(inflater: MenuInflater) {
            itemView.setOnCreateContextMenuListener { menu, v, menuInfo ->
                currentView = WeakReference(itemView)
                inflater.inflate(R.menu.pivot_context_profile, menu)
            }
        }

        companion object {
            var currentView: WeakReference<View>? = null
        }
    }

    inner class GraphAdapter(graph: Graph) : BaseGraphAdapter<ViewHolder>(graph) {

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

                viewHolder.setScriptContextMenu(activity!!.menuInflater, data.hasSuccessors)
            } else {
                viewHolder as SimpleViewHolder
                viewHolder.layoutScriptExtra.visibility = View.GONE
                if (data is ProfileInfo) {
                    viewHolder.tvName.text = data.name
                    viewHolder.setBackgroundColor(resources.getColor(R.color.nodeBackground_Profile))
                    viewHolder.setProfileContextMenu(activity!!.menuInflater)
                } else if (data is ServiceStart) {
                    viewHolder.tvName.text = getString(data.nameId())
                    viewHolder.setBackgroundColor(resources.getColor(R.color.nodeBackground_VirtualRoot))
                }
            }
        }
    }
}
