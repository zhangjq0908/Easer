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

package ryey.easer.core.ui.data.script.script_tree_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;
import ryey.easer.core.data.ScriptTree;
import ryey.easer.core.data.storage.ScriptDataStorage;
import ryey.easer.core.ui.data.DataListContainerInterface;
import ryey.easer.core.ui.data.DataListInterface;
import ryey.easer.core.ui.data.script.EditScriptActivity;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

public class ScriptTreeListFragment extends Fragment implements DataListInterface {

    private static final String TAG = "[ScriptTreeList]";

    WeakReference<DataListContainerInterface> refContainer;

    ScriptDataStorage scriptDataStorage;

    RecyclerViewWithContext recyclerView;
    TreeViewAdapterWithContextMenu adapter;
    List<TreeNode> scriptTreeNodeList;

    private EventItem mCurrentEventItem;

    @NonNull
    @Override
    public String title() {
        return getString(R.string.title_script);
    }

    @Override
    public int helpTextRes() {
        return R.string.help_script;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_script_tree_extra, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_plain_list) {
            refContainer.get().switchContent(DataListContainerInterface.ListType.script);
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scriptDataStorage = new ScriptDataStorage(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_script_tree_list, container, false);

        getActivity().setTitle(title());
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recyclerView_script);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(recyclerView);
        recyclerView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                getActivity().getMenuInflater().inflate(R.menu.list_context, menu);
            }
        });

        scriptTreeNodeList = convertScriptTreeToView(scriptDataStorage.getScriptTrees());
        adapter = new TreeViewAdapterWithContextMenu(scriptTreeNodeList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnLongItemClickListener(new TreeViewAdapterWithContextMenu.onLongItemClickListener() {
            @Override
            public void ItemLongClicked(View v, EventItem eventItem) {
                mCurrentEventItem = eventItem;
                v.showContextMenu();
            }
        });
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode treeNode, RecyclerView.ViewHolder viewHolder) {
                if (!treeNode.isLeaf()) {
                    onToggle(!treeNode.isExpand(), viewHolder);
                }
                return false;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder viewHolder) {
                TreeViewAdapterWithContextMenu.ViewHolder eventViewHolder = (TreeViewAdapterWithContextMenu.ViewHolder) viewHolder;
                int rotateDegree = isExpand ? 90 : -90;
                eventViewHolder.ivArrow
                        .animate()
                        .rotationBy(rotateDegree)
                        .start();
            }
        });

        return view;
    }

    private void reloadList() {
        scriptTreeNodeList.clear();
        scriptTreeNodeList = convertScriptTreeToView(scriptDataStorage.getScriptTrees());
        adapter.refresh(scriptTreeNodeList);

        if (adapter.getItemCount() == 0) {
            Logger.d("%s: no item", TAG);
            refContainer.get().setShowHelp(true);
        } else {
            Logger.d("%s: has item", TAG);
            refContainer.get().setShowHelp(false);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        scriptDataStorage = null;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String name = mCurrentEventItem.eventName;
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit:
                refContainer.get().editData(name);
                return true;
            case R.id.action_delete:
                refContainer.get().deleteData(name);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private static List<TreeNode> convertScriptTreeToView(List<ScriptTree> scriptTrees) {
        return convertScriptTreeToView(scriptTrees, new ArrayList<TreeNode>(), null);
    }

    private static List<TreeNode> convertScriptTreeToView(List<ScriptTree> scriptTrees, List<TreeNode> nodes, TreeNode parent) {
        for (ScriptTree scriptTree : scriptTrees) {
            EventItem item = new EventItem(scriptTree.getName(), scriptTree.isActive(), scriptTree.getData().isValid());
            TreeNode<EventItem> node = new TreeNode<>(item);
            node.setParent(parent);
            if (scriptTree.getSubs().size() != 0)
                node.setChildList(convertScriptTreeToView(scriptTree.getSubs(), new ArrayList<TreeNode>(), node));
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public void registerContainer(@NonNull DataListContainerInterface container) {
        this.refContainer = new WeakReference<>(container);
    }

    @Override
    public Intent intentForEditDataActivity() {
        return new Intent(getContext(), EditScriptActivity.class);
    }

    @Override
    public void onEditDataResultCallback(boolean success) {
        if (success) {
            onDataChangedFromEditDataActivity();
        }
    }

    protected void onDataChangedFromEditDataActivity() {
        reloadList();
    }
}
