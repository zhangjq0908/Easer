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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ryey.easer.R;
import ryey.easer.commons.plugindef.operationplugin.OperationData;
import ryey.easer.core.ProfileLoaderIntentService;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.operation.state_control.StateControlOperationData;
import ryey.easer.plugins.operation.state_control.StateControlOperationPlugin;

public class ProfileListFragment extends AbstractDataListFragment<ProfileDataStorage> {

    static {
        TAG = "[ProfileListFragment] ";
    }

    @Override
    protected String title() {
        return getString(R.string.title_profile);
    }

    @Override
    protected int helpTextRes() {
        return R.string.help_profile;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_profile, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListDataWrapper wrapper = (ListDataWrapper) getListView().getItemAtPosition(info.position);
        String name = wrapper.name;
        int id = item.getItemId();
        if (id == R.id.action_trigger_profile) {
            ProfileLoaderIntentService.triggerProfile(getContext(), name);
            return true;
        } else
            return super.onContextItemSelected(item);
    }

    @Override
    protected List<ListDataWrapper> queryDataList() {
        ProfileDataStorage dataStorage = ProfileDataStorage.getInstance(getContext());
        List<ListDataWrapper> dataWrapperList = new ArrayList<>();
        for (String name : dataStorage.list()) {
            ProfileStructure profile = dataStorage.get(name);
            boolean valid = profile.isValid();
            if (valid) {
                Collection<OperationData> stateControlOperationData = profile.get(new StateControlOperationPlugin().id());
                if (stateControlOperationData.size() > 0) {
                    for (OperationData operationData : stateControlOperationData) {
                        if (!((StateControlOperationData) operationData).isValid(getContext()))
                            valid = false;
                    }
                }
            }
            if (valid) {
                dataWrapperList.add(new ListDataWrapper(name));
            } else {
                dataWrapperList.add(new ListDataWrapper(name, R.color.colorText_invalid));
            }
        }
        return dataWrapperList;
    }

    @Override
    protected Intent intentForEditDataActivity() {
        return new Intent(getActivity(), EditProfileActivity.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //noinspection ConstantConditions
        if (PluginRegistry.getInstance().operation().getEnabledPlugins(getContext()).size() == 0) {
            FloatingActionButton fab = view.findViewById(R.id.fab);
            fab.hide();
        }
    }
}
