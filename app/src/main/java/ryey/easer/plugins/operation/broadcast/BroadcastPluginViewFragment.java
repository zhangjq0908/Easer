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

package ryey.easer.plugins.operation.broadcast;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.PluginViewFragment;

public class BroadcastPluginViewFragment extends PluginViewFragment<BroadcastOperationData> {
    private EditText m_text_action;
    private EditText m_text_category;
    private EditText m_text_type;
    private EditText m_text_data;
    private List<ExtraItemFragment> m_fragment_extra = new ArrayList<>();

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__broadcast, container, false);
        m_text_action = view.findViewById(R.id.text_action);
        m_text_category = view.findViewById(R.id.text_category);
        m_text_type = view.findViewById(R.id.text_type);
        m_text_data = view.findViewById(R.id.text_data);
        view.findViewById(R.id.button_add_extra).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtraItemFragment fragment = new ExtraItemFragment();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.layout_extras, fragment)
                        .commit();
                m_fragment_extra.add(fragment);
            }
        });

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull BroadcastOperationData data) {
        IntentData rdata = data.data;
        m_text_action.setText(rdata.action);
        m_text_category.setText(Utils.StringCollectionToString(rdata.category, false));
        m_text_type.setText(rdata.type);
        if (rdata.data != null)
            m_text_data.setText(rdata.data.toString());
        if (rdata.extras != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            if (m_fragment_extra.size() > 0) {
                for (ExtraItemFragment fragment : m_fragment_extra) {
                    transaction.remove(fragment);
                }
                m_fragment_extra.clear();
            }
            for (int i = 0; i < rdata.extras.size(); i++) {
                IntentData.ExtraItem item = rdata.extras.get(i);
                ExtraItemFragment fragment = ExtraItemFragment.createInstance(item);
                transaction.add(R.id.layout_extras, fragment);
                m_fragment_extra.add(fragment);
            }
            transaction.commit();
        }
    }

    @ValidData
    @NonNull
    @Override
    public BroadcastOperationData getData() throws InvalidDataInputException {
        IntentData data = new IntentData();
        data.action = m_text_action.getText().toString();
        data.category = Utils.stringToStringList(m_text_category.getText().toString());
        data.type = m_text_type.getText().toString();
        data.data = Uri.parse(m_text_data.getText().toString());
        if (m_fragment_extra.size() > 0) {
            List<IntentData.ExtraItem> extraItemList = new ArrayList<>(m_fragment_extra.size());
            for (ExtraItemFragment fragment : m_fragment_extra) {
                extraItemList.add(fragment.getData());
            }
            data.extras = extraItemList;
        }
        BroadcastOperationData broadcastOperationData = new BroadcastOperationData(data);
        return broadcastOperationData;
    }

    public static class ExtraItemFragment extends Fragment {

        IntentData.ExtraItem item;

        static ExtraItemFragment createInstance(IntentData.ExtraItem item) {
            ExtraItemFragment fragment = new ExtraItemFragment();
            fragment.item = item;
            return fragment;
        }

        EditText editText_key, editText_value;
        Spinner spinner_type;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.plugin_operation__broadcast_fragment_extra_item, container, false);
            editText_key = view.findViewById(R.id.editText_key);
            editText_value = view.findViewById(R.id.editText_value);
            spinner_type = view.findViewById(R.id.spinner_type);

            if (item != null) {
                editText_key.setText(item.key);
                editText_value.setText(item.value);
                String[] types = getResources().getStringArray(R.array.extra_type);
                for (int i = 0; i < types.length; i++) {
                    if (types[i].equals(item.type)) {
                        spinner_type.setSelection(i);
                    }
                }
            }

            return view;
        }

        IntentData.ExtraItem getData() {
            if (Utils.isBlank(editText_key.getText().toString()))
                return null;
            String key = editText_key.getText().toString().trim();
            String value = editText_value.getText().toString();
            String type = (String) spinner_type.getSelectedItem();
            return new IntentData.ExtraItem(key, value, type);
        }

    }
}
