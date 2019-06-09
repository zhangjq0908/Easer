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

package ryey.easer.core.ui.data;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.SourceCategory;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public abstract class SourceSelectorDialogFragment<S extends Skill & SourceCategory.Categorized> extends DialogFragment {

    private SelectedListener<S> selectedListener = null;

    private List<SkillItemWrapper<S>> availableLocalPluginList;

    @StringRes protected abstract int titleRes();
    protected abstract List<S> skillList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_select_skill, container, false);
        {
            TextView tvTitle = view.findViewById(android.R.id.title);
            tvTitle.setText(titleRes());
        }
        StickyListHeadersListView list = view.findViewById(android.R.id.list);
        List<S> localSkillList = skillList();
        availableLocalPluginList = new ArrayList<>(localSkillList.size());
        for (S operationSkill : localSkillList) {
            availableLocalPluginList.add(new SkillItemWrapper<>(operationSkill.id(),
                    operationSkill.view().desc(getResources()),
                    operationSkill.category(),
                    operationSkill)
            );
        }
        PluginListAdapter adapter = new PluginListAdapter(getContext(), availableLocalPluginList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SkillItemWrapper<S> skillItemWrapper = (SkillItemWrapper<S>) parent.getItemAtPosition(position);
                if (!skillItemWrapper.isRemote()) {
                    S plugin = skillItemWrapper.skill;
                    if (plugin.checkPermissions(getContext())) {
                        selectedListener.onSelected(skillItemWrapper);
                        dismiss();
                    } else {
                        plugin.requestPermissions(getActivity(), 0);
                    }
                } else {
                    selectedListener.onSelected(skillItemWrapper);
                    dismiss();
                }
            }
        });
        return view;
    }

    public void setSelectedListener(SelectedListener<S> listener) {
        this.selectedListener = listener;
    }

    public interface SelectedListener<S extends Skill> {
        void onSelected(SkillItemWrapper<S> skillItemWrapper);
    }

    public static class SkillItemWrapper<S extends Skill> {
        @NonNull public final String id;
        @NonNull public final String name;
        @NonNull public final SourceCategory category;
        @Nullable public final S skill;
        SkillItemWrapper(@NonNull String id, @NonNull String name, @NonNull SourceCategory category, @Nullable S skill) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.skill = skill;
        }
        public boolean isRemote() {
            return skill == null;
        }
        public String toString() {
            return name;
        }
    }

    public class PluginListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        private final List<SkillItemWrapper<S>> operationList = new ArrayList<>();
        private LayoutInflater inflater;

        PluginListAdapter(Context context, List<SkillItemWrapper<S>> originalList) {
            inflater = LayoutInflater.from(context);
            this.operationList.addAll(originalList);
            Collections.sort(operationList, new Comparator<SkillItemWrapper<S>>() {
                @Override
                public int compare(SkillItemWrapper<S> skillItemWrapper, SkillItemWrapper<S> t1) {
                    return skillItemWrapper.category.ordinal() - t1.category.ordinal();
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
