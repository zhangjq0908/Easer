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

package ryey.easer.core.ui.data.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ryey.easer.R;
import ryey.easer.commons.local_plugin.operationplugin.OperationPlugin;
import ryey.easer.core.RemoteOperationPluginInfo;
import ryey.easer.core.RemotePluginCommunicationHelper;
import ryey.easer.plugin.operation.Category;
import ryey.easer.plugins.LocalPluginRegistry;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class OperationSelectorFragment extends DialogFragment {

    SelectedListener selectedListener = null;
    Map<Class<? extends OperationPlugin>, Integer> addedPlugins = new ArrayMap<>();

    List<OperationPluginItemWrapper> availableLocalPluginList;

    RemotePluginCommunicationHelper helper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //noinspection ConstantConditions
        helper = new RemotePluginCommunicationHelper(getContext());
        helper.begin();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_select_operation_plugin, container, false);
        StickyListHeadersListView list = view.findViewById(android.R.id.list);
        List<OperationPlugin> localOperationPluginList = LocalPluginRegistry.getInstance().operation().getEnabledPlugins(getContext());
        availableLocalPluginList = new ArrayList<>(localOperationPluginList.size());
        for (OperationPlugin operationPlugin : localOperationPluginList) {
            if (addedPlugins.containsKey(operationPlugin.getClass())) {
                if (operationPlugin.maxExistence() > 0) {
                    if (addedPlugins.get(operationPlugin.getClass()) >= operationPlugin.maxExistence())
                        continue;
                }
            }
            availableLocalPluginList.add(new OperationPluginItemWrapper(operationPlugin.id(),
                    operationPlugin.view().desc(getResources()),
                    operationPlugin.category(),
                    operationPlugin)
            );
        }
        PluginListAdapter adapter = new PluginListAdapter(getContext(), availableLocalPluginList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OperationPluginItemWrapper operationPluginItemWrapper = (OperationPluginItemWrapper) parent.getItemAtPosition(position);
                if (!operationPluginItemWrapper.isRemote()) {
                    OperationPlugin plugin = operationPluginItemWrapper.plugin;
                    if (plugin.checkPermissions(getContext())) {
                        selectedListener.onSelected(operationPluginItemWrapper);
                        dismiss();
                    } else {
                        plugin.requestPermissions(getActivity(), 0);
                    }
                } else {
                    selectedListener.onSelected(operationPluginItemWrapper);
                    dismiss();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        helper.asyncCurrentOperationPluginList(new RemotePluginCommunicationHelper.OnOperationPluginListObtainedCallback() {
            @Override
            public void onListObtained(Set<RemoteOperationPluginInfo> operationPluginInfos) {
                StickyListHeadersListView list = view.findViewById(android.R.id.list);
                List<OperationPluginItemWrapper> descList = new ArrayList<>(availableLocalPluginList);
                for (RemoteOperationPluginInfo remotePluginInfo : operationPluginInfos) {
                    descList.add(new OperationPluginItemWrapper(
                            remotePluginInfo.getPluginId(),
                            remotePluginInfo.getPluginName(),
                            remotePluginInfo.getCategory(),
                            null));
                }
                PluginListAdapter adapter = new PluginListAdapter(getContext(), descList);
                list.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.end();
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
        void onSelected(OperationPluginItemWrapper operationPluginItemWrapper);
    }

    protected static class OperationPluginItemWrapper {
        @NonNull final String id;
        @NonNull final String name;
        @NonNull final Category category;
        @Nullable final OperationPlugin plugin;
        OperationPluginItemWrapper(@NonNull String id, @NonNull String name, @NonNull Category category, @Nullable OperationPlugin plugin) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.plugin = plugin;
        }
        public boolean isRemote() {
            return plugin == null;
        }
        public String toString() {
            return name;
        }
    }

    public class PluginListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        private final List<OperationPluginItemWrapper> operationList = new ArrayList<>();
        private LayoutInflater inflater;

        PluginListAdapter(Context context, List<OperationPluginItemWrapper> originalList) {
            inflater = LayoutInflater.from(context);
            this.operationList.addAll(originalList);
            Collections.sort(operationList, new Comparator<OperationPluginItemWrapper>() {
                @Override
                public int compare(OperationPluginItemWrapper operationPluginItemWrapper, OperationPluginItemWrapper t1) {
                    return operationPluginItemWrapper.category.ordinal() - t1.category.ordinal();
                }
            });
        }

        @Override
        public int getCount() {
            return operationList.size();
        }

        @Override
        public Object getItem(int position) {
            return operationList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_operation, parent, false);
                holder.text = convertView.findViewById(R.id.tv_operation_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(operationList.get(position).name);

            return convertView;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.item_header, parent, false);
                holder.text = convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            //set header text as first char in name
            String headerText = operationList.get(position).category.toString(getResources());
            holder.text.setText(headerText);
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            //return the first character of the country as ID because this is what headers are based upon
            return operationList.get(position).category.ordinal();
        }

        class HeaderViewHolder {
            TextView text;
        }

        class ViewHolder {
            TextView text;
        }

    }
}
