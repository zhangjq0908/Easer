/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;
import ryey.easer.core.data.ScriptTree;
import ryey.easer.core.data.storage.ScriptDataStorage;
import ryey.easer.core.ui.data.DataListContainerFragment;
import ryey.easer.core.ui.data.DataListContainerInterface;
import ryey.easer.core.ui.data.DataListInterface;
import ryey.easer.core.ui.data.script.EditScriptActivity;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

public class ScriptTreeListFragment extends Fragment implements DataListInterface {

    private static final String TAG = "[ScriptTreeList]";

    DataListContainerInterface container;

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
            container.switchContent(DataListContainerInterface.ListType.script);
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
        adapter = new TreeViewAdapterWithContextMenu(scriptTreeNodeList);
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

        getActivity().setTitle(title());

        setHasOptionsMenu(true);

        return view;
    }

    private void reloadList() {
        scriptTreeNodeList.clear();
        scriptTreeNodeList = convertScriptTreeToView(scriptDataStorage.getScriptTrees(), scriptTreeNodeList);
        adapter.refresh(scriptTreeNodeList);

        if (adapter.getItemCount() == 0) {
            Logger.d("%s: no item", TAG);
            container.setShowHelp(true);
        } else {
            Logger.d("%s: has item", TAG);
            container.setShowHelp(false);
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
                container.editData(name);
                return true;
            case R.id.action_delete:
                container.deleteData(name);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private static List<TreeNode> convertScriptTreeToView(List<ScriptTree> scriptTrees) {
        return convertScriptTreeToView(scriptTrees, new ArrayList<TreeNode>());
    }

    private static List<TreeNode> convertScriptTreeToView(List<ScriptTree> scriptTrees, List<TreeNode> nodes) {
        for (ScriptTree scriptTree : scriptTrees) {
            EventItem item = new EventItem(scriptTree.getName());
            TreeNode<EventItem> node = new TreeNode<>(item);
            if (scriptTree.getSubs().size() != 0)
                node.setChildList(convertScriptTreeToView(scriptTree.getSubs(), new ArrayList<TreeNode>()));
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public void registerContainer(@NonNull DataListContainerFragment container) {
        this.container = container;
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
