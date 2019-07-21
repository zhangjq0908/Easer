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

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.Logger;

import ryey.easer.R;
import ryey.easer.core.ui.data.condition.ConditionListFragment;
import ryey.easer.core.ui.data.event.EventListFragment;
import ryey.easer.core.ui.data.profile.ProfileListFragment;
import ryey.easer.core.ui.data.script.ScriptListFragment;
import ryey.easer.core.ui.data.script.script_tree_list.ScriptTreeListFragment;

public final class DataListContainerFragment extends Fragment implements DataListContainerInterface {

    private static final String ARG_LIST_TYPE = "list_type";

    private static final int request_code = 10;

    private TextView tv_help;

    private Fragment currentFragment;
    private DataListInterface currentDataList;

    public static DataListContainerFragment create(ListType listType) {
        Logger.i("going to create DataListFragment %s", listType);
        DataListContainerFragment fragment = new DataListContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_TYPE, listType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container_data_list, container, false);

        tv_help = view.findViewById(R.id.help_text);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newData();
            }
        });

        if (currentFragment == null) {
            Bundle args = getArguments();
            assert args != null;
            ListType listType = (ListType) args.getSerializable(ARG_LIST_TYPE);
            assert listType != null;
            switchContent(listType);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_data, menu);
        assert currentDataList != null;
        Integer extra = currentDataList.extraMenu();
        if (extra != null)
            inflater.inflate(extra, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            Dialog dialog = new AlertDialog.Builder(getContext())
                    .setNeutralButton(R.string.button_ok, null)
                    .setMessage(currentDataList.helpTextRes())
                    .create();
            dialog.show();
            ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            return true;
        }
        return false;
    }

    @Override
    public void setShowHelp(boolean show) {
        if (show) {
            tv_help.setVisibility(View.VISIBLE);
            tv_help.setText(currentDataList.helpTextRes());
        } else {
            tv_help.setVisibility(View.GONE);
        }
    }

    public void newData() {
        Intent intent = currentDataList.intentForEditDataActivity();
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.add);
        startActivityForResult(intent, request_code);
    }
    public void editData(@NonNull String name) {
        Intent intent = currentDataList.intentForEditDataActivity();
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.edit);
        intent.putExtra(EditDataProto.CONTENT_NAME, name);
        startActivityForResult(intent, request_code);
    }
    public void deleteData(@NonNull String name) {
        Intent intent = currentDataList.intentForEditDataActivity();
        intent.putExtra(EditDataProto.PURPOSE, EditDataProto.Purpose.delete);
        intent.putExtra(EditDataProto.CONTENT_NAME, name);
        startActivityForResult(intent, request_code);
    }

    @Override
    public void switchContent(@NonNull ListType type) {
        switch (type) {
            case script:
                currentFragment = new ScriptListFragment();
                break;
            case script_tree:
                currentFragment = new ScriptTreeListFragment();
                break;
            case event:
                currentFragment = new EventListFragment();
                break;
            case condition:
                currentFragment = new ConditionListFragment();
                break;
            case profile:
                currentFragment = new ProfileListFragment();
                break;
            default:
                throw new IllegalStateException("Unexpected List Fragment type");
        }
        currentDataList = (DataListInterface) currentFragment;
        currentDataList.registerContainer(this);

        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.data_list, currentFragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_code) {
            currentDataList.onEditDataResultCallback(resultCode == Activity.RESULT_OK);
        }
    }
}
