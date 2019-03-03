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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ryey.easer.R;

public abstract class AbstractDataListFragment extends ListFragment implements DataListInterface {

    protected static String TAG = "[AbstractDataListFragment] ";

    protected WeakReference<DataListContainerInterface> refContainer;

    @NonNull
    public abstract String title();

    @StringRes
    public abstract int helpTextRes();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ListAdapter adapter = new IListAdapter(getActivity(), new ArrayList<ListDataWrapper>());
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fab_list, container, false);
        getActivity().setTitle(title());
        setHasOptionsMenu(true);
        return view;
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        reloadList();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ListDataWrapper wrapper = (ListDataWrapper) l.getItemAtPosition(position);
        refContainer.get().editData(wrapper.name);
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
                refContainer.get().editData(name);
                return true;
            case R.id.action_delete:
                refContainer.get().deleteData(name);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    protected abstract List<ListDataWrapper> queryDataList();

    @Override
    public void registerContainer(@NonNull DataListContainerInterface container) {
        this.refContainer = new WeakReference<>(container);
    }

    protected void reloadList() {
        Logger.d(TAG + "reloadList()");
        List<ListDataWrapper> items = queryDataList();
        Logger.v(TAG + "All item: %s", items);
        IListAdapter adapter = (IListAdapter) getListAdapter();
        adapter.clear();
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
        if (getListAdapter().getCount() == 0) {
            Logger.d("%s: no item", TAG);
            refContainer.get().setShowHelp(true);
        } else {
            Logger.d("%s: has item", TAG);
            refContainer.get().setShowHelp(false);
        }
    }

    public abstract Intent intentForEditDataActivity();

    @Override
    public void onEditDataResultCallback(boolean success) {
        if (success) {
            onDataChangedFromEditDataActivity();
        }
    }

    @CallSuper
    protected void onDataChangedFromEditDataActivity() {
        reloadList();
    }

    protected static class IListAdapter extends ArrayAdapter<ListDataWrapper> {
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

    protected static class ListDataWrapper {
        public final String name;
        final @ColorRes int colorRes;
        public ListDataWrapper(String name) {
            this.name = name;
            colorRes = R.color.colorText;
        }
        public ListDataWrapper(String name, @ColorRes int colorRes) {
            this.name = name;
            this.colorRes = colorRes;
        }
    }
}

