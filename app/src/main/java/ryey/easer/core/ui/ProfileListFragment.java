/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import ryey.easer.R;
import ryey.easer.core.data.storage.DataStorage;
import ryey.easer.core.data.storage.xml.profile.ProfileXmlDataStorage;

public class ProfileListFragment extends ListFragment {
    public static final String ACTION_DATA_CHANGED = "ryey.easer.ACTION.PROFILE_DATA_CHANGED";

    DataStorage<ryey.easer.core.data.ProfileStructure> mStorage = null;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_DATA_CHANGED.equals(action)) {
                reloadList();
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(getString(R.string.title_profile));
        IntentFilter filter = new IntentFilter(ACTION_DATA_CHANGED);
        activity.registerReceiver(receiver, filter);
    }

    @Override
    public void onDetach() {
        getActivity().unregisterReceiver(receiver);
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        registerForContextMenu(getListView());

        try {
            mStorage = ProfileXmlDataStorage.getInstance(getActivity());
            List<String> items = mStorage.list();
            Log.d(getClass().getSimpleName(), "items: " + items);
            ListAdapter adapter = new ProfileListAdapter(getActivity(), items);
            setListAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadList(); //TODO: 尚有重複載入，待改進
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String name = (String) l.getItemAtPosition(position);
        beginEditProfile(name);
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
                beginNewProfile();
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
                beginEditProfile(name);
                return true;
            case R.id.action_delete:
                begingDeleteProfile(name);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void reloadList() {
        Log.d(getClass().getSimpleName(), "reloadList");
        List<String> items = mStorage.list();
        Log.d(getClass().getSimpleName(), "items: " + items);
        ProfileListAdapter adapter = (ProfileListAdapter) getListAdapter();
        adapter.clear();
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
    }

    private void beginNewProfile() {
        DialogFragment dialogFragment = EditProfileDialogFragment.newInstance(EditProfileDialogFragment.Purpose.add, null);
        dialogFragment.show(getFragmentManager().beginTransaction(), "newProfile");
    }
    private void beginEditProfile(String name) {
        DialogFragment dialogFragment = EditProfileDialogFragment.newInstance(EditProfileDialogFragment.Purpose.edit, name);
        dialogFragment.show(getFragmentManager().beginTransaction(), "editProfile");
    }
    private void begingDeleteProfile(String name) {
        DialogFragment dialogFragment = EditProfileDialogFragment.newInstance(EditProfileDialogFragment.Purpose.delete, name);
        dialogFragment.show(getFragmentManager().beginTransaction(), "deleteProfile");
    }
}

//class ProfileListAdapter extends SimpleAdapter {
//    public ProfileListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
//        super(context, data, resource, from, to);
//    }
//}
class ProfileListAdapter extends ArrayAdapter<String> {
    public ProfileListAdapter(Context context, List<String> data) {
        super(context, R.layout.item_profile, R.id.textView_profile_title, data);
    }
}
