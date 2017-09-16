/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.core.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;

import ryey.easer.R;
import ryey.easer.core.EHService;
import ryey.easer.core.data.storage.EventDataStorage;
import ryey.easer.core.data.storage.xml.event.XmlEventDataStorage;

public class EventListFragment extends ListFragment {
    static final int request_code = 10;

    EventDataStorage mStorage = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(getString(R.string.title_event));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        registerForContextMenu(getListView());

        List<String> items = null;
        try {
            mStorage = XmlEventDataStorage.getInstance(getActivity());
            items = mStorage.list();
            Logger.v("All events: %s", items);
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventListAdapter adapter = new EventListAdapter(getActivity(), items);
        setListAdapter(adapter);
        reloadList(); //TODO: 尚有重複載入，待改進
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String name = (String) l.getItemAtPosition(position);
        beginEditEvent(name);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.list_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                beginNewEvent();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        String name = (String) getListView().getItemAtPosition(info.position);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit:
                beginEditEvent(name);
                return true;
            case R.id.action_delete:
                beginDeleteEvent(name);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void reloadList() {
        Logger.d("reloadList()");
        List<String> items = mStorage.list();
        Logger.v("All events: %s", items);
        EventListAdapter adapter = (EventListAdapter) getListAdapter();
        adapter.clear();
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
        Intent intent = new Intent();
        intent.setAction(EHService.ACTION_RELOAD);
        getActivity().sendBroadcast(intent);
    }

    private void beginNewEvent() {
        Intent intent = new Intent(getActivity(), EditEventActivity.class);
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.add);
        startActivityForResult(intent, request_code);
    }
    private void beginEditEvent(String name) {
        Intent intent = new Intent(getActivity(), EditEventActivity.class);
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.edit);
        intent.putExtra(EditDataProto.CONTENT_NAME, name);
        startActivityForResult(intent, request_code);
    }
    private void beginDeleteEvent(String name) {
        Intent intent = new Intent(getActivity(), EditEventActivity.class);
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.delete);
        intent.putExtra(EditDataProto.CONTENT_NAME, name);
        startActivityForResult(intent, request_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_code) {
            if (resultCode == Activity.RESULT_OK) {
                reloadList();
            }
        }
    }
}

class EventListAdapter extends ArrayAdapter<String> {
    public EventListAdapter(Context context, List<String> data) {
        super(context, R.layout.item_event, R.id.textView_event_title, data);
    }
}