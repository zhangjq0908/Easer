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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;
import ryey.easer.core.data.storage.AbstractDataStorage;

abstract class AbstractDataListFragment<T extends AbstractDataStorage> extends ListFragment {

    protected static String TAG = "[AbstractDataListFragment] ";

    static final int request_code = 10;

    protected abstract String title();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());

        ListAdapter adapter = new IListAdapter(getActivity(), new ArrayList<ListDataWrapper>());
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(title());
        View view = inflater.inflate(R.layout.fragment_fab_list, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginNewData();
            }
        });

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ListDataWrapper wrapper = (ListDataWrapper) l.getItemAtPosition(position);
        beginEditData(wrapper.name);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListDataWrapper wrapper = (ListDataWrapper) getListView().getItemAtPosition(info.position);
        String name = wrapper.name;
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit:
                beginEditData(name);
                return true;
            case R.id.action_delete:
                begingDeleteData(name);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    protected abstract List<ListDataWrapper> queryDataList();

    protected void reloadList() {
        Logger.d(TAG + "reloadList()");
        List<ListDataWrapper> items = queryDataList();
        Logger.v(TAG + "All item: %s", items);
        IListAdapter adapter = (IListAdapter) getListAdapter();
        adapter.clear();
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @CallSuper
    protected void onDataChangedFromEditDataActivity() {
        reloadList();
    }

    protected abstract Intent intentForEditDataActivity();

    private void beginNewData() {
        Intent intent = intentForEditDataActivity();
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.add);
        startActivityForResult(intent, request_code);
    }
    private void beginEditData(String name) {
        Intent intent = intentForEditDataActivity();
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.edit);
        intent.putExtra(EditDataProto.CONTENT_NAME, name);
        startActivityForResult(intent, request_code);
    }
    private void begingDeleteData(String name) {
        Intent intent = intentForEditDataActivity();
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.delete);
        intent.putExtra(EditDataProto.CONTENT_NAME, name);
        startActivityForResult(intent, request_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_code) {
            if (resultCode == Activity.RESULT_OK) {
                onDataChangedFromEditDataActivity();
            }
        }
    }

    static class IListAdapter extends ArrayAdapter<ListDataWrapper> {
        IListAdapter(Context context, List<ListDataWrapper> data) {
            super(context, R.layout.item_data_list, R.id.textView_data_title, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ListDataWrapper wrapper = getItem(position);
            TextView tv_name = view.findViewById(R.id.textView_data_title);
            tv_name.setTextColor(ContextCompat.getColor(getContext(), wrapper.colorRes));
            tv_name.setText(wrapper.name);
            return view;
        }
    }

    static class ListDataWrapper {
        final String name;
        final @ColorRes int colorRes;
        ListDataWrapper(String name) {
            this.name = name;
            colorRes = R.color.colorText;
        }
        ListDataWrapper(String name, @ColorRes int colorRes) {
            this.name = name;
            this.colorRes = colorRes;
        }
    }
}

