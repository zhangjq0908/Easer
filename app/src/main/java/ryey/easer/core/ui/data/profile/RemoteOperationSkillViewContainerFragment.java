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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.core.RemotePluginCommunicationHelper;
import ryey.easer.core.RemotePluginInfo;
import ryey.easer.core.ui.data.AbstractSkillDataFragment;
import ryey.easer.remote_plugin.RemoteOperationData;
import ryey.easer.remote_plugin.RemotePlugin;

public class RemoteOperationSkillViewContainerFragment extends AbstractSkillDataFragment<RemoteOperationData> {

    public static final String ARG_ID = "ryey.easer.core.ui.data.profile.RemoteOperationSkillViewContainerFragment.args.ID";
    public static final String ARG_DATA = "ryey.easer.core.ui.data.profile.RemoteOperationSkillViewContainerFragment.args.TYPE";

    public static RemoteOperationSkillViewContainerFragment createInstance(String pluginId, @Nullable RemoteOperationData data) {
        RemoteOperationSkillViewContainerFragment fragment = new RemoteOperationSkillViewContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ID, pluginId);
        bundle.putParcelable(ARG_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    private static final int REQCODE_EDIT_DATA = 10;

    private CheckBox mCheckBox;

    RemotePluginCommunicationHelper helper;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQCODE_EDIT_DATA) {
            if (resultCode == Activity.RESULT_OK) {
                data.setExtrasClassLoader(RemoteOperationData.class.getClassLoader());
                passed_data = data.getParcelableExtra(RemotePlugin.EXTRA_DATA);
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //noinspection ConstantConditions
        helper = new RemotePluginCommunicationHelper(getContext());
        helper.begin();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remote_plugin, container, false);

        mCheckBox = view.findViewById(R.id.checkbox_pluginview_enabled);
        Button button = view.findViewById(R.id.button_edit);
        assert getArguments() != null;
        passed_data = getArguments().getParcelable(ARG_DATA);
        final String id = getArguments().getString(ARG_ID);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.asyncRemoteEditOperationData(id, new RemotePluginCommunicationHelper.OnEditDataIntentObtainedCallback() {
                    @Override
                    public void onEditDataIntentObtained(@NonNull Intent editDataIntent) {
                        editDataIntent.putExtra(RemotePlugin.EXTRA_DATA, passed_data);
                        startActivityForResult(editDataIntent, REQCODE_EDIT_DATA);
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView tv_name = view.findViewById(R.id.tv_plugin_name);
        final String id = getArguments().getString(ARG_ID);
        helper.asyncFindPlugin(id, new RemotePluginCommunicationHelper.OnPluginFoundCallback() {
            @Override
            public void onPluginFound(@Nullable RemotePluginInfo info) {
                tv_name.setText(info.getPluginName());
            }
        });
    }

    @Override
    protected void _fill(@NonNull RemoteOperationData data) {
        mCheckBox.setChecked(true);
        passed_data = data;
    }

    @NonNull
    @Override
    public RemoteOperationData getData() throws InvalidDataInputException {
        if (mCheckBox.isChecked()) {
            if (passed_data != null)
                return passed_data;
            throw new InvalidDataInputException();
        } else {
            throw new IllegalStateException("The view should be checked as \"enabled\" before getting its data");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.end();
    }

    String id() {
        return getArguments().getString(ARG_ID);
    }

    boolean isEnabled() {
        return mCheckBox.isChecked();
    }
}
