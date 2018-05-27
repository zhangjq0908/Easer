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

package ryey.easer.core.ui.data;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ryey.easer.R;
import ryey.easer.commons.plugindef.operationplugin.OperationPlugin;
import ryey.easer.plugins.PluginRegistry;

public class OperationSelectorFragment extends DialogFragment {

    SelectedListener selectedListener = null;
    Map<Class<? extends OperationPlugin>, Integer> addedPlugins = new ArrayMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_select_operation_plugin, container, false);
        ListView list = view.findViewById(android.R.id.list);
        List<OperationPlugin> operationPluginList = PluginRegistry.getInstance().operation().getEnabledPlugins(getContext());
        List<PluginItemWrapper> descList = new ArrayList<>(operationPluginList.size());
        for (OperationPlugin operationPlugin : operationPluginList) {
            if (addedPlugins.containsKey(operationPlugin.getClass())) {
                if (operationPlugin.maxExistence() > 0) {
                    if (addedPlugins.get(operationPlugin.getClass()) >= operationPlugin.maxExistence())
                        continue;
                }
            }
            descList.add(new PluginItemWrapper(operationPlugin.view().desc(getResources()), operationPlugin));
        }
        ArrayAdapter<PluginItemWrapper> adapter = new ArrayAdapter<>(getContext(), R.layout.item_operation, R.id.tv_operation_name, descList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OperationPlugin plugin = ((PluginItemWrapper) parent.getItemAtPosition(position)).plugin;
                if (plugin.checkPermissions(getContext())) {
                    selectedListener.onSelected(plugin);
                    dismiss();
                } else {
                    plugin.requestPermissions(getActivity(), 0);
                }
            }
        });
        return view;
    }

    synchronized void addSelectedPlugin(@NonNull OperationPlugin plugin) {
        Class<? extends OperationPlugin> klass = plugin.getClass();
        if (addedPlugins.containsKey(klass)) {
            addedPlugins.put(klass, addedPlugins.get(klass) + 1);
        } else {
            addedPlugins.put(klass, 1);
        }
    }

    void setSelectedListener(SelectedListener listener) {
        this.selectedListener = listener;
    }

    interface SelectedListener {
        void onSelected(OperationPlugin plugin);
    }

    protected static class PluginItemWrapper {
        final String name;
        final OperationPlugin plugin;
        PluginItemWrapper(String name, OperationPlugin plugin) {
            this.name = name;
            this.plugin = plugin;
        }
        public String toString() {
            return name;
        }
    }
}
